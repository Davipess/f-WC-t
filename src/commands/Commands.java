package commands;

/**
 * Available console commands.
 * Separated into standard student commands and privileged administration commands.
 */
public enum Commands {
    // Comandos de utilizador (Estudante)
    REGISTER("register [id] [nome] [password] - regista uma nova conta de estudante"),
    LOGIN("login <id> [password] - inicia sessao"),
    LOGOUT("logout - encerra a sessao ativa"),
    WHOAMI("whoami - mostra o utilizador autenticado"),
    BEST("best [edificio] [piso] - encontra a melhor casa de banho operacional"),
    INFO("info <id> - detalhes e estado de uma casa de banho"),
    LIST("list - lista as casas de banho do campus"),
    REVIEW("review <id> <estrelas 0-5> [comentario] - avalia uma casa de banho"),
    ALERT("alert [id] [tipo] - reporta aviso de limpeza ou manutencao"),
    CONFIRM("confirm <id> - confirma aviso pendente"),
    ALERTS("alerts - lista avisos ativos no campus"),
    CONTEST("contest - apresenta o ranking semanal de casas de banho"),
    HELP("help - mostra os comandos disponiveis"),
    EXIT("exit - encerra a aplicacao"),

    // Comandos de administracao e manutencao
    REOPEN("reopen <id> - reabre instalacao apos resolucao (Admin)"),
    ADDBATHROOM("addbathroom <id> <edificio> <piso> <genero> [local] - regista casa de banho (Admin)"),
    ADDBUILDING("addbuilding <code> <nome> - regista novo edificio (Admin)"),
    ADVANCECONTEST("advancecontest - avanca a semana do concurso (Admin)"),
    USERS("users - lista todos os utilizadores (Admin)"),
    BUILDINGS("buildings - lista todos os edificios do campus"),
    UNKNOWN("");

    private final String description;

    Commands(String description) {
        this.description = description;
    }

    /**
     * Gets the command description and usage syntax.
     * @return usage string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Parses a command string case-insensitively and maps condensed aliases.
     * @param text input command token
     * @return matching Commands enum constant
     */
    public static Commands fromString(String text) {
        if (text == null) {
            return UNKNOWN;
        }
        String clean = text.trim().toUpperCase();
        return switch (clean) {
            case "REGISTER" -> REGISTER;
            case "LOGIN" -> LOGIN;
            case "LOGOUT" -> LOGOUT;
            case "WHOAMI" -> WHOAMI;
            case "BEST", "FINDBEST", "BUILDING" -> BEST;
            case "INFO", "BATHROOM", "REVIEWS" -> INFO;
            case "LIST", "AVAILABLE" -> LIST;
            case "REVIEW" -> REVIEW;
            case "ALERT" -> ALERT;
            case "CONFIRM" -> CONFIRM;
            case "ALERTS" -> ALERTS;
            case "CONTEST", "TOP", "BUILDINGRANK" -> CONTEST;
            case "HELP" -> HELP;
            case "EXIT" -> EXIT;
            case "REOPEN", "RESOLVE" -> REOPEN;
            case "ADDBATHROOM", "ADDACCESSIBLE" -> ADDBATHROOM;
            case "ADDBUILDING" -> ADDBUILDING;
            case "ADVANCECONTEST" -> ADVANCECONTEST;
            case "USERS" -> USERS;
            case "BUILDINGS" -> BUILDINGS;
            default -> UNKNOWN;
        };
    }
}
