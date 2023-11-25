package com.neo.gpt_bot_test.commands;

public interface TextCommands {
    String START = "start";
    String START_DESCRIPTION = "get started";

    String MY_DATA = "my_data";

    String MD_DESCRIPTION = "get info about user";

    String DELETE_MY_DATA = "delete_my_data";

    String DMD_DESCRIPTION = "remove all info about user";

    String HELP = "help";

    String HELP_DESCRIPTION = "get full commands list";

    String SET_TEMPERATURE = "set_temperature";

    String SET_TEMPERATURE_DESCRIPTION = "set AI model creativity level";

    String SET_AI_CONTEXT = "set_ai_context";

    String SET_AI_CONTEXT_DESCRIPTION = "set AI model profile (role)";
}
