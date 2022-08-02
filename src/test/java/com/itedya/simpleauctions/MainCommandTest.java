package com.itedya.simpleauctions;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.itedya.simpleauctions.commands.Main;
import com.itedya.simpleauctions.commands.SubCommand;
import com.itedya.simpleauctions.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MainCommandTest {
    private ServerMock server;
    private SimpleAuctions plugin;

    @BeforeAll
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SimpleAuctions.class);
    }

    @AfterAll
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testCommandExistance() {
        Command command = plugin.getCommand("licytacje");
        assert command != null;
        PlayerMock playerMock = server.addPlayer();
        SubCommand subCommand = new Main();

        subCommand.onCommand(playerMock, command, "licytacje", new String[]{});

        playerMock.assertSaid(ChatUtil.UNKNOWN_COMMAND);
        playerMock.assertNoMoreSaid();
    }
}
