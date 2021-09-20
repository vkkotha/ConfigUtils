package org.configutils;

import org.apache.commons.cli.*;

import java.io.IOException;

public class PropertiesDiffCli {
    private final CommandFactory commandFactory;
    private final String[] args;

    PropertiesDiffCli(CommandFactory commandFactory, String[] args) {
        this.commandFactory = commandFactory;
        this.args = args;
    }

    private static void printUsage(Options options) {
        new HelpFormatter().printHelp("java -jar config-utils-<version>-all.jar <file1> <file2> [OPTIONS]", options);
    }

    private Options setupCommandOptions() {
        Options options = new Options();
        options.addOption(new Option("h", "help", false, "Print help"));
        Option diffOption = new Option("d", true, "Use diff tool [diff executable]");
        diffOption.setOptionalArg(true);
        options.addOption(diffOption);
        Option reportOption = new Option("r", true, "Generate report [text | html]");
        reportOption.setOptionalArg(true);
        options.addOption(reportOption);
        return options;
    }

    public void execute() {
        Options options = setupCommandOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, this.args);
            if (cmd.hasOption("h")) {
                printUsage(options);
            }

            if (cmd.getArgs().length != 2) {
                throw new ParseException("Invalid arguments");
            }

            Command command;
            if (cmd.hasOption("d")) {
                command = commandFactory.getPropertiesDiffToolCommand(cmd.getOptionValue("d"));
                command.execute(cmd.getArgs());
            } else {
                command = commandFactory.getPropertiesReportGeneratorCommand(cmd.getOptionValue("r"));
                command.execute(cmd.getArgs());
            }
        } catch (final ParseException pe) {
            printUsage(options);
        } catch (IOException ie) {
            throw new RuntimeException(ie);
        }
    }

    public static void main(String[] args) {
        CommandFactory commandFactory = new CommandFactory();
        PropertiesDiffCli cli = new PropertiesDiffCli(commandFactory, args);
        cli.execute();
    }
}

