package com.github.oobila.bukkit.command;

import com.github.oobila.bukkit.chat.Message;
import com.github.oobila.bukkit.command.arguments.Argument;
import com.github.oobila.bukkit.command.arguments.EnumArg;
import com.github.oobila.bukkit.command.arguments.StringArg;
import com.github.oobila.bukkit.command.validators.ValidationResponse;
import com.github.oobila.bukkit.command.validators.Validator;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.oobila.bukkit.command.BoolStatement.bool;

@Getter
public class Command implements CommandExecutor, TabCompleter {

    private static final Message NO_PERMISSION = Message.builder("You do not have permission to run this command");
    private static final Message TOO_MANY_ARGS = Message.builder("You have entered too many arguments for this command");
    private static final Message NOT_ENOUGH_ARGS = Message.builder("You have not entered enough arguments for this command");
    private static final Message ARG_IS_NON_NUMERIC = Message.builder("arg: {0} is not numeric");
    private static final Message ARG_IS_NON_BOOLEAN = Message.builder("arg: {0} is not a boolean");
    private static final Message ARG_IS_NON_ENUM_OPTION = Message.builder("arg: {0} is not an option\noptions: \n");
    private static final Message PLAYER_IS_NOT_ONLINE = Message.builder("arg: player \"{0}\" is not online");

    private final String name;
    private final String description;
    private final List<ArgumentBase<?,?>> arguments = new ArrayList<>();
    private Command parent = null;
    private List<String> aliases;
    private String permission;
    private PlayerCommandExecutor commandExecutor;
    private GameCommandExecutor gameCommand;
    private final Map<String, Command> subCommands = new HashMap<>();
    private final Map<String, Command> subCommandsWithAliases = new HashMap<>();

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Command aliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    public Command permission(String permission) {
        this.permission = permission;
        return this;
    }

    public Command command(PlayerCommandExecutor command) {
        this.commandExecutor = command;
        return this;
    }

    public Command command(GameCommandExecutor command) {
        this.gameCommand = command;
        return this;
    }

    public <T, S extends ArgumentBase<T, ?>> ArgumentBase<T, S> arg(ArgumentBase<T, S> argument) {
        argument.validate(arguments);
        argument.position = arguments.size();
        arguments.add(argument);
        return argument;
    }

    public StringArg arg(String name) {
        return new StringArg(name);
    }

    public <T extends Enum<T>> EnumArg<T> arg(
            String name,
            Class<T> type
    ) {
        return new EnumArg<>(name, type);
    }

    public <T> Argument<T> arg(
            String name,
            Class<T> type,
            ArgumentBase.ArgumentDeserializer<T> deserializer
    ) {
        return arg(name, type, deserializer, null, true);
    }

    public <T> Argument<T> arg(
            String name,
            Class<T> type,
            ArgumentBase.ArgumentDeserializer<T> deserializer,
            T defaultValue,
            boolean mandatory
    ) {
        Argument<T> argument = new Argument<>(name, type, deserializer)
                .defaultValue(defaultValue)
                .mandatory(mandatory);
        arg(argument);
        return argument;
    }

    public Command subCommand(Command subCommand) {
        subCommand.parent = this;
        this.subCommands.put(subCommand.getName(), subCommand);
        this.subCommandsWithAliases.put(subCommand.getName(), subCommand);
        if (subCommand.getAliases() != null) {
            subCommand.getAliases().forEach(aka ->
                    this.subCommandsWithAliases.put(aka, subCommand)
            );
        }
        return this;
    }

    public void register(JavaPlugin plugin) {
        PluginCommand pluginCommand = plugin.getCommand(name);
        if (pluginCommand == null) {
            throw new NullPointerException("command " + name + " does not exist");
        }
        pluginCommand.setAliases(aliases);
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length != 0 && subCommandsWithAliases.containsKey(args[0])) {
            //is sub command
            return subCommandsWithAliases.get(args[0]).onCommand(
                    sender,
                    command,
                    args[0],
                    Arrays.copyOfRange(args, 1, args.length)
            );
        } else if (!hasPermission(sender)) {
            //sender does not have permission
            return true;
        }
        if (!(sender instanceof Player player)) {
            //sender is not player
            this.gameCommand.onCommand(this, label, args);
        } else {
            //sender is player
            if (validate(args, player)){
                this.commandExecutor.onCommand(player, this, label, args);
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length != 0 && subCommandsWithAliases.containsKey(args[0])) {
            //is sub command
            return subCommandsWithAliases.get(args[0]).onTabComplete(
                    sender,
                    command,
                    args[0],
                    Arrays.copyOfRange(args, 1, args.length)
            );
        } else if (sender instanceof Player player) {
            if (!hasPermission(sender) || args.length == 0) {
                return Collections.emptyList();
            }
            if (!arguments.isEmpty() && arguments.size() >= args.length) {
                ArgumentBase<?,?> argument = arguments.get(args.length - 1);
                if (argument.getFixedSuggestions() != null && !argument.getFixedSuggestions().isEmpty()) {
                    return argument.getFixedSuggestions().stream()
                            .map(Object::toString)
                            .filter(s ->
                                    args[args.length - 1].length() > 0 ?
                                            StringUtils.containsIgnoreCase(s, args[args.length - 1]) :
                                            true
                            ).toList();
                } else if (argument.getSuggestionCallable() != null) {
                    return argument.getSuggestionCallable()
                            .getSuggestions(player, args[args.length - 1]).stream()
                            .map(Object::toString).toList();
                }
            }
            if (args.length == 1 && !subCommands.isEmpty()) {
                return subCommandsWithAliases.keySet().stream().toList();
            }
        }
        return Collections.emptyList();
    }

    private boolean hasPermission(CommandSender commandSender) {
        if (commandExecutor != null && commandSender instanceof Player player) {
            return bool(checkPermission(player))
                    .onFalse(() -> NO_PERMISSION.send(player));
        } else {
            return gameCommand != null;
        }
    }

    private boolean checkPermission(Player player) {
        if (player.isOp()) {
            return true;
        } else if (permission != null) {
            return player.hasPermission(permission);
        } else if (parent != null) {
            return parent.checkPermission(player);
        } else {
            return false;
        }
    }

    private boolean validate(String[] args, Player player) {
        if (args.length == 0 && arguments.isEmpty()) {
            //no arguments required
            return true;
        } else if (args.length > arguments.size()) {
            TOO_MANY_ARGS.send(player);
            return false;
        } else {
            for(int i=0; i < arguments.size(); i++) {
                if (!validArgument(i, args, player)) {
                    sendHelpMessage(player);
                    return false;
                }
            }
            return true;
        }
    }

    private boolean validArgument(int i, String[] args, Player player) {
        if(isHelpMessage(i, args)) {
            //send help command
            return false;
        }
        ArgumentBase<?,?> argument = arguments.get(i);
        if (!argument.isMandatory() && args.length <= i) {
            //reached non-mandatory arguments
            return true;
        }
        if (args.length <= i) {
            NOT_ENOUGH_ARGS.send(player);
            return false;
        }
        String input = args[i];
        ValidationResponse response = handleValidation(argument, input);
        if (response != null && !response.getResult()) {
            if (response.getMessage() != null) {
                response.getMessage().send(player);
            }
            return false;
        }
        return true;
    }

    private boolean isHelpMessage(int i, String[] args) {
        return i == 0 &&
                args.length > 0 &&
                (args[0].equalsIgnoreCase("help") || args[0].equals("?"));
    }

    private <T> ValidationResponse handleValidation(ArgumentBase<T,?> argument, String input) {
        ValidationResponse response = null;
        if (argument.getType().equals(String.class)) {
            return null;
        } else if (argument.getType().isEnum()) {
            if (argument.getMin() == null) {
                response = CommandManager.getEnumValidator()
                        .isValid(input, argument.getType());
            } else {
                response = CommandManager.getEnumValidator()
                        .isValid(
                                input,
                                argument.getType(),
                                argument.getMin(),
                                argument.getMax()
                        );
            }
        } else {
            Validator validator = CommandManager.getValidators().get(argument.getType());
            if (validator == null) {
                response = ValidationResponse.failed(
                        Message.builder("No validator exists for type: {0}")
                                .arg(argument.getType().getName())
                );
            } else if (argument.getMin() == null) {
                response = CommandManager.getValidators().get(argument.getType())
                        .isValid(input, argument.getType(), argument.getMin(), argument.getMax());
            } else {
                response = CommandManager.getValidators().get(argument.getType())
                        .isValid(
                                input,
                                argument.getType(),
                                argument.getMin(),
                                argument.getMax()
                        );
            }
        }
        return response;
    }

    public void sendHelpMessage(Player player) {
        getHelpMessage(this).send(player);
    }

    private static String getParentPath(Command parent, String path) {
        if (parent != null) {
            return getParentPath(parent.getParent(), parent.name + " " + path);
        } else {
            return path;
        }
    }

    private static Message getHelpMessage(Command command) {
        String parentPath = getParentPath(command.parent, command.name);
        return Message.builder("---- {0} help ----")
                .colors(ChatColor.YELLOW, ChatColor.YELLOW)
                .arg(parentPath)
                .append(
                        getHelpMessageSegment(command.arguments, parentPath, command.description)
                )
                .append(() ->
                        command.subCommands.values().stream().map(c ->
                                getHelpMessageSegment(
                                        c.arguments,
                                        getParentPath(c.parent, c.name),
                                        c.description
                                )
                        ).toList()
                );
    }

    private static Message getHelpMessageSegment(List<ArgumentBase<?,?>> arguments, String parentPath, String description) {
        return Message.builder("{0} {1} - {2}")
                .arg(ChatColor.WHITE, "/" + parentPath)
                .arg(ChatColor.GRAY, arguments.stream().map(argument -> "[" + argument.getName() + "]")
                        .collect(Collectors.joining(" ")))
                .arg(ChatColor.GOLD, description);
    }
}
