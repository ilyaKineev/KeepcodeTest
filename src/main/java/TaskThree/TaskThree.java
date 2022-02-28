package TaskThree;

public class TaskThree {
    void processTask(ChannelHandlerContext ctx) {
        InetSocketAddress lineAddress = createInetSocketAddress();
        for (Command currentCommand : getAllCommands()) {

            switch (currentCommand.getCommandType()){
                case (CommandType.REBOOT_CHANNEL):
                    reboootChannel(ctx,lineAddress,currentCommand);
                    break;
                default:
                    defaultChannel(ctx,lineAddress,currentCommand);
                    break;
            }
        }
        sendKeepAliveOkAndFlush(ctx);
    }

    private void defaultChannel(ChannelHandlerContext ctx, InetSocketAddress lineAddress, Command currentCommand) {
        if (!currentCommand.isAttemptsNumberExhausted()) {
            sendCommandToContext(ctx, lineAddress, currentCommand.getCommandText());

            try {
                AdminController.getInstance().processUssdMessage(createDblIncomeUssdMessage(lineAddress,currentCommand), false);
            } catch (Exception ignored) { }

            Log.ussd.write(String.format(&quot;sent: ip: %s; порт: %d; %s&quot;,
            lineAddress.getHostString(), lineAddress.getPort(), currentCommand.getCommandText()));
            currentCommand.setSendDate(new Date());
            currentCommand.incSendCounter();
        } else {
            deleteCommand(currentCommand.getCommandType());
        }
    }

    private void reboootChannel(ChannelHandlerContext ctx, InetSocketAddress lineAddress, Command currentCommand) {
        if (!currentCommand.isAttemptsNumberExhausted() & currentCommand.isTimeToSend()) {
            sendCommandToContext(ctx, lineAddress, currentCommand.getCommandText());
            try {
                AdminController.getInstance().processUssdMessage(createDblIncomeUssdMessage(lineAddress,currentCommand), false);
            } catch (Exception ignored) { }

            currentCommand.setSendDate(new Date());
            Log.ussd.write(String.format(&quot;sent: ip: %s; порт: %d; %s&quot;,
            lineAddress.getHostString(), lineAddress.getPort(), currentCommand.getCommandText()));
            currentCommand.incSendCounter();
        } else {
            deleteCommand(currentCommand.getCommandType());
        }
    }

    private Object createDblIncomeUssdMessage(InetSocketAddress lineAddress, Command currentCommand) {
        return new DblIncomeUssdMessage(lineAddress.getHostName(), lineAddress.getPort(), 0,
                EnumGoip.getByModel(getGoipModel()), currentCommand.getCommandText());
    }

    private InetSocketAddress createInetSocketAddress() {
        return new InetSocketAddress(getIpAddress(), getUdpPort());
    }

}

