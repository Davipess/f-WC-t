import alerts.Alert;
import alerts.AlertType;
import bathrooms.Bathroom;
import bathrooms.BathroomStatus;
import buildings.Building;
import commands.Commands;
import exceptions.*;
import reviews.Review;
import users.Role;
import users.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main console interface for the fWCt platform.
 * Handles user input and executes commands with immediate feedback.
 * All user interface strings and formatting templates are defined as constants.
 */
public class Main {

    // General application strings
    private static final String PROMPT = "fWCt> ";
    private static final String UNKNOWN_COMMAND_MSG = "Unknown command. Type 'help' to see available commands.";
    private static final String BYE_MSG = "Thank you for using fWCt. See you next time!";
    private static final String BANNER_BORDER = "===========================================================";
    private static final String BANNER_HEADER = "        fWCt - NOVA FCT BATHROOM MANAGEMENT SYSTEM        ";
    private static final String BANNER_HINT = "Type 'help' to consult commands or 'best' to search.\n";
    private static final String EMPTY_STRING = "";
    private static final String SPACE_STRING = " ";
    private static final String HYPHEN_STRING = "-";

    // Parsing regex and selection options
    private static final String REGEX_WHITESPACE = "\\s+";
    private static final String OPTION_1 = "1";
    private static final String OPTION_2 = "2";
    private static final String OPTION_3 = "3";
    private static final String OPTION_4 = "4";

    // Gender string constants
    private static final String GENDER_MALE = "MALE";
    private static final String GENDER_FEMALE = "FEMALE";
    private static final String GENDER_UNISEX = "UNISEX";
    private static final String GENDER_ACCESSIBLE = "ACCESSIBLE";
    private static final String GENDER_M_SHORT = "M";
    private static final String GENDER_F_SHORT = "F";
    private static final String GENDER_U_SHORT = "U";
    private static final String GENDER_A_SHORT = "A";
    private static final String GENDER_MASCULINO = "MASCULINO";
    private static final String GENDER_HOMEM = "HOMEM";
    private static final String GENDER_FEMININO = "FEMININO";
    private static final String GENDER_MULHER = "MULHER";
    private static final String GENDER_UNISSEXO = "UNISSEXO";
    private static final String GENDER_ACESSIVEL = "ACESSIVEL";

    // Role string constants
    private static final String ROLE_STUDENT = "STUDENT";
    private static final String ROLE_ADMIN = "ADMIN";

    // Alert type string constants
    private static final String ALERT_TYPE_CLEANING = "CLEANING";
    private static final String ALERT_TYPE_MAINTENANCE = "MAINTENANCE";

    // Formatting templates
    private static final String SCORE_MAX_SUFFIX = "/5";
    private static final String SCORE_DECIMAL_FORMAT = "%.1f";
    private static final String RATING_COUNT_TEMPLATE = "%s/5 (%d%s)";
    private static final String FLOOR_PREFIX = "Floor ";
    private static final String FLOOR_FILTER_FORMAT = " (Floor %d)";
    private static final String LOCATION_PAREN_FORMAT = " (%s)";

    // Input prompt messages
    private static final String PROMPT_REQUIRED_FIELD = "Please fill in this field.";
    private static final String PROMPT_INT_OUT_OF_RANGE = "Please enter a value between %d and %d.\n";
    private static final String PROMPT_INT_INVALID = "Invalid number. Enter a value between %d and %d.\n";
    private static final String PROMPT_USER_ID = "Username / ID (e.g., 74167 or bernardo): ";
    private static final String PROMPT_USER_NAME = "Full name: ";
    private static final String PROMPT_ADMIN_ROLE_MENU = "[Administration] Choose role for new account:\n  1: STUDENT\n  2: ADMIN";
    private static final String ADMIN_ONLY_ROLE_MSG = "Error: Only administrators can create ADMIN accounts.";
    private static final String ADMIN_ONLY_ACTION_MSG = "Error: Only administrators can perform this operation.";
    private static final String ADMIN_ONLY_REOPEN_MSG = "Error: Only administrators can reopen bathrooms.";
    private static final String PROMPT_ROLE_CHOICE = "Option (1/2 or name): ";
    private static final String PROMPT_PASSWORD = "Choose a password: ";
    private static final String PROMPT_CONFIRM_PASSWORD = "Confirm password: ";
    private static final String PASSWORDS_MISMATCH_MSG = "Error: Passwords do not match. Registration cancelled.";
    private static final String INVALID_USER_DATA_MSG = "Error: Invalid user data.";
    private static final String PROMPT_LOGIN_ID = "Username / ID: ";
    private static final String PROMPT_LOGIN_PASSWORD = "Password: ";
    private static final String WRONG_PASSWORD_MSG = "Error: Incorrect password.";

    // User command outputs
    private static final String USER_REGISTERED_MSG = "User '%s' (%s) registered successfully.\n";
    private static final String USER_ALREADY_EXISTS_MSG = "Error: A user with ID '%s' already exists.\n";
    private static final String USER_NOT_FOUND_MSG = "Error: User '%s' not found.\n";
    private static final String LOGIN_SUCCESS_MSG = "Logged in as %s (%s).\n";
    private static final String LOGOUT_SUCCESS_MSG = "Logged out successfully.\n";
    private static final String NO_ACTIVE_USER_MSG = "Error: This operation requires logging in first (use 'login <id>' or 'login').";
    private static final String WHOAMI_LAYOUT_MSG = "Current user: %s (%s) [%s]\n";
    private static final String NO_USER_LOGGED_IN_MSG = "No user currently authenticated.";
    private static final String USER_LIST_HEADER_MSG = "--- Registered Users ---";
    private static final String USER_ENTRY_LAYOUT_MSG = "- [%s] %s (%s) - %d reviews, %d alerts\n";

    // Building inputs and outputs
    private static final String PROMPT_BUILDING_CODE = "Building code (e.g., VII, DI): ";
    private static final String PROMPT_BUILDING_NAME = "Full building name: ";
    private static final String BUILDING_REQUIRED_FIELDS_MSG = "Error: Building code and name are required.";
    private static final String BUILDING_ADDED_MSG = "Building [%s] '%s' registered successfully.\n";
    private static final String BUILDING_ALREADY_EXISTS_MSG = "Error: Building [%s] already exists.\n";
    private static final String BUILDING_NOT_FOUND_MSG = "Error: Building '%s' not found.\n";
    private static final String BUILDING_LIST_HEADER_MSG = "--- Campus Buildings ---";
    private static final String BUILDING_ENTRY_LAYOUT_MSG = "- [%s] %s (%d bathrooms)\n";

    // Bathroom creation inputs and outputs
    private static final String PROMPT_BATHROOM_ID = "Bathroom ID (e.g., DI-0-M1): ";
    private static final String PROMPT_BATHROOM_BUILDING = "Building code (e.g., DI, VII): ";
    private static final String PROMPT_BATHROOM_FLOOR = "Floor (e.g., 0, 1): ";
    private static final String PROMPT_BATHROOM_GENDER = "Gender (MALE, FEMALE, UNISEX): ";
    private static final String PROMPT_BATHROOM_LOCATION = "Specific location (e.g., Main Hall, Labs Corridor, Enter to omit): ";
    private static final String PROMPT_ACCESSIBLE_ID = "Accessible bathroom ID (e.g., VII-1-A): ";
    private static final String PROMPT_ACCESSIBLE_GENDER = "Gender (MALE, FEMALE, UNISEX, ACCESSIBLE): ";
    private static final String BATHROOM_ADDED_MSG = "Bathroom '%s' registered in building %s (Floor %d).\n";
    private static final String BATHROOM_ALREADY_EXISTS_MSG = "Error: A bathroom with ID '%s' already exists.\n";
    private static final String BATHROOM_NOT_FOUND_MSG = "Error: Bathroom '%s' does not exist.\n";
    private static final String NO_BATHROOMS_FOUND_MSG = "No bathrooms found matching the criteria.";

    // Interactive search messages
    private static final String PROMPT_SEARCH_BUILDING = "Building or department (e.g., DI, VII, DM): ";
    private static final String PROMPT_SEARCH_FLOOR = "Floor (e.g., 0, 1, or Enter for all floors): ";
    private static final String PROMPT_SEARCH_GENDER_MENU = "Choose gender:\n  1: MALE\n  2: FEMALE\n  3: UNISEX\n  (or Enter for any gender)";
    private static final String PROMPT_SEARCH_GENDER_CHOICE = "Option (1/2/3 or name): ";
    private static final String SEARCH_AVAILABLE_HEADER_LAYOUT = "Available bathrooms in %s%s:\n";
    private static final String SEARCH_ENTRY_LAYOUT = "  #%d [%s] %s [%s] - %s%s\n";
    private static final String BEST_BADGE = " (Best choice)";
    private static final String SEARCH_NOT_FOUND_LAYOUT = "No operational bathrooms found in building '%s' matching the criteria.\nTip: Try checking nearby buildings or other floors.\n";

    // Cards and layout helpers
    private static final String CARD_DETAIL_HEADER_LAYOUT = "[%s] %s - Floor %d%s\n";
    private static final String BEST_CARD_DIRECT_HEADER_LAYOUT = "BEST OPTION: %s [%s]\n";
    private static final String CARD_FIELD_ID_LAYOUT = "  Identifier:  %s\n";
    private static final String CARD_FIELD_LOCATION_LAYOUT = "  Location:    Floor %d%s\n";
    private static final String CARD_FIELD_LOCATION_FULL_LAYOUT = "  Location:    %s, Floor %d%s\n";
    private static final String CARD_FIELD_TYPE_LAYOUT = "  Type:        %s%s\n";
    private static final String CARD_FIELD_STATUS_LAYOUT = "  Status:      %s\n";
    private static final String CARD_FIELD_RATING_LAYOUT = "  Rating:      %s\n";
    private static final String CARD_ALERTS_EMPTY = "  Alerts:      No active alerts\n";
    private static final String CARD_ALERTS_HEADER = "  Active alerts:\n";
    private static final String CARD_ALERT_ITEM_LAYOUT = "    - %s\n";
    private static final String CARD_ALERT_DETAIL_ITEM_LAYOUT = "    - %s (%s)\n";
    private static final String CARD_REVIEWS_HEADER = "  Latest reviews:\n";
    private static final String CARD_REVIEW_ITEM_LAYOUT = "    - %s (%d/5): \"%s\"\n";

    // Status and labels
    private static final String STATUS_AVAILABLE_LABEL = "Available";
    private static final String STATUS_CLEANING_LABEL = "Cleaning in progress";
    private static final String STATUS_MAINTENANCE_LABEL = "Maintenance in progress";
    private static final String RATING_EMPTY_LABEL = "No reviews yet";
    private static final String RATING_COUNT_SINGLE_LABEL = " review";
    private static final String RATING_COUNT_PLURAL_LABEL = " reviews";
    private static final String ACCESSIBLE_BADGE_LABEL = " [Accessible]";

    // Review messages
    private static final String PROMPT_REVIEW_RATING = "Rating (1 to 5 stars): ";
    private static final String PROMPT_REVIEW_COMMENT = "Comment (optional, press Enter to skip): ";
    private static final String DEFAULT_COMMENT = "No additional comment.";
    private static final String REVIEW_ADDED_MSG = "Review registered successfully for bathroom '%s'.\n";
    private static final String INVALID_RATING_MSG = "Error: Rating must be an integer between 0 and 5.";
    private static final String DUPLICATE_REVIEW_MSG = "Error: You have already reviewed this bathroom in current contest week (Week %d).\n";
    private static final String NO_REVIEWS_MSG = "This bathroom does not have any reviews yet.";
    private static final String REVIEWS_HEADER_LAYOUT = "--- Reviews for Bathroom %s ---\n";
    private static final String REVIEW_HISTORY_ITEM_LAYOUT = "- %s (%d/5) [Week %d]: \"%s\"\n";

    // Alert messages and prompts
    private static final String PROMPT_ALERT_TYPE_MENU = "Choose alert type:\n  1: CLEANING\n  2: MAINTENANCE";
    private static final String PROMPT_ALERT_TYPE_CHOICE = "Option (1/2 or name): ";
    private static final String ALERT_CLEANING_DEFAULT_DESC = "Cleaning in progress";
    private static final String ALERT_MAINTENANCE_DEFAULT_DESC = "Maintenance or breakdown";
    private static final String ALERT_CREATED_MSG = "Alert created for '%s': %s.\n";
    private static final String ALERT_PENDING_MSG = "Alert created for '%s' (Pending: needs 1 more confirmation to lock bathroom).\n";
    private static final String ALERT_CONFIRMED_SUCCESS_MSG = "Alert confirmed for '%s' (2/2 confirmations reached. Bathroom locked).\n";
    private static final String ALERT_ALREADY_CONFIRMED_MSG = "Error: You have already confirmed this alert.\n";
    private static final String CONFIRM_RECORDED_MSG = "Confirmation recorded for '%s'.\n";
    private static final String RESOLUTION_VOTE_MSG = "Reopen vote recorded for '%s' (1/2 votes. Needs 1 more vote to reopen).\n";
    private static final String ALERT_RESOLVED_MSG = "Alert resolved for '%s'! Bathroom reopened and available.\n";
    private static final String ALERT_NOT_FOUND_MSG = "Error: No active or pending alert found for '%s'.\n";
    private static final String ALERT_ALREADY_RESOLVED_MSG = "Error: The alert for '%s' is already resolved.\n";
    private static final String NO_ACTIVE_ALERTS_MSG = "There are no active alerts on campus at this time.";
    private static final String ALERTS_HEADER_MSG = "--- Active Campus Alerts ---";
    private static final String ALERT_CAMPUS_ROW_LAYOUT = "- %s: %s [%s]\n";
    private static final String ALERT_CONFIRMED_STATUS_LABEL = "Confirmed (2/2)";
    private static final String ALERT_PENDING_STATUS_LAYOUT = "Pending (%d/%d)";
    private static final String PROMPT_CONFIRM_BATHROOM = "Bathroom to confirm (e.g., DI-0-M1): ";
    private static final String PROMPT_REOPEN_BATHROOM = "Bathroom to reopen (e.g., DI-0-M1): ";

    // Contest messages
    private static final String CONTEST_HEADER_LAYOUT = "Weekly Contest: Week #%d\n";
    private static final String CONTEST_EMPTY_MSG = "  No reviews recorded yet for this week's contest.";
    private static final String CONTEST_ROW_LAYOUT = "  #%d %-9s (Building %s%s) - %s\n";
    private static final String CONTEST_ADVANCED_LAYOUT = "Contest advanced to Week #%d. New reviews unlocked!\n";

    // Listing headers and formats
    private static final String LIST_ALL_HEADER = "--- Campus Bathrooms ---";
    private static final String LIST_AVAILABLE_HEADER = "--- Operational / Available Bathrooms ---";
    private static final String LIST_BUILDING_HEADER_LAYOUT = "--- Bathrooms in Building: %s (%s) ---\n";
    private static final String LIST_GENDER_HEADER_LAYOUT = "--- Bathrooms by Gender: %s ---\n";
    private static final String TOP_HEADER_LAYOUT = "--- Top %d Bathrooms ---\n";
    private static final String TOP_ROW_LAYOUT = "  #%d %-9s (Building %s, Floor %d%s) - %s\n";
    private static final String NO_REVIEWS_RECORDED_MSG = "No bathrooms have received reviews yet.";
    private static final String BUILDING_RANK_HEADER = "--- Building Rating Ranking ---";
    private static final String NO_BUILDING_REVIEWS_MSG = "No reviews recorded across campus buildings yet.";
    private static final String BUILDING_RANK_ROW_LAYOUT = "- %-28s : %s\n";
    private static final String BATHROOM_ROW_LAYOUT = "%-9s : Building %s, Floor %d%s [%s%s] | Status: %s | %s\n";

    // Help text
    private static final String HELP_CONTENT = """
            ===========================================================
                                 AVAILABLE COMMANDS                    
            ===========================================================
            [User Commands]
              best [building] [floor]             - find best operational bathroom
              info <id>                           - details, status and reviews of a facility
              list                                - list campus bathrooms
              review <id> <rating> [comment]      - review a bathroom (0 to 5)
              alert [id] [type]                   - report cleaning or maintenance alert
              confirm <id>                        - confirm a peer's pending alert
              alerts                              - view all active alerts on campus
              contest                             - view weekly ranking and top bathrooms

            [Account & Session]
              register [id] [name] [password]     - register new student account
              login <id> [password]               - log in to account
              logout                              - end active session
              whoami                              - show current authenticated user

            [Administration & Maintenance] (Admin)
              reopen <id>                         - reopen resolved bathroom (Admin)
              addbathroom <id> <bld> <flr> <gen>  - register new bathroom (Admin)
              addbuilding <code> <name>           - register new building (Admin)
              advancecontest                      - advance contest week (Admin)
              users                               - list registered users (Admin)
              buildings                           - list campus buildings

            [General]
              help                                - show this command menu
              exit                                - exit application
            ===========================================================""";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FWCt system = new FWCtClass();

        System.out.println(BANNER_BORDER);
        System.out.println(BANNER_HEADER);
        System.out.println(BANNER_BORDER);
        System.out.print(BANNER_HINT);

        executeLoop(sc, system);
        sc.close();
    }

    private static void executeLoop(Scanner sc, FWCt system) {
        while (true) {
            System.out.print(PROMPT);
            if (!sc.hasNextLine()) {
                break;
            }
            String rawLine = sc.nextLine().trim();
            if (rawLine.isEmpty()) {
                continue;
            }

            String[] parts = rawLine.split(REGEX_WHITESPACE, 2);
            String cmdStr = parts[0].toUpperCase();
            String args = EMPTY_STRING;
            if (parts.length > 1) {
                args = parts[1].trim();
            }

            Commands cmd = Commands.fromString(cmdStr);
            if (cmd == Commands.UNKNOWN) {
                System.out.println(UNKNOWN_COMMAND_MSG);
                continue;
            }

            if (cmd == Commands.EXIT) {
                System.out.println(BYE_MSG);
                break;
            }

            switch (cmd) {
                case REGISTER -> handleRegister(sc, system, args);
                case LOGIN -> handleLogin(sc, system, args);
                case LOGOUT -> handleLogout(system);
                case WHOAMI -> handleWhoAmI(system);
                case BEST -> handleFindBest(sc, system, args);
                case INFO -> handleBathroomDetails(sc, system, args);
                case LIST -> handleListAll(system);
                case REVIEW -> handleAddReview(sc, system, args);
                case ALERT -> handleAddAlert(sc, system, args);
                case CONFIRM -> handleConfirmAlert(sc, system, args);
                case ALERTS -> handleListAlerts(system);
                case CONTEST -> handleContestStatus(system);
                case HELP -> handleHelp();
                case REOPEN -> handleResolveAlert(sc, system, args);
                case ADDBATHROOM -> handleAddBathroom(sc, system, args);
                case ADDBUILDING -> handleAddBuilding(sc, system, args);
                case ADVANCECONTEST -> handleAdvanceContest(system);
                case USERS -> handleUsers(system);
                case BUILDINGS -> handleListBuildings(system);
                case EXIT, UNKNOWN -> {}
            }
        }
    }

    // Input prompt helper methods

    private static String promptNonEmpty(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            if (!sc.hasNextLine()) {
                return EMPTY_STRING;
            }
            String val = sc.nextLine().trim();
            if (!val.isEmpty()) {
                return val;
            }
            System.out.println(PROMPT_REQUIRED_FIELD);
        }
    }

    private static String promptOptional(Scanner sc, String message) {
        System.out.print(message);
        if (!sc.hasNextLine()) {
            return EMPTY_STRING;
        }
        return sc.nextLine().trim();
    }

    private static int promptInt(Scanner sc, String message, int min, int max) {
        while (true) {
            System.out.print(message);
            if (!sc.hasNextLine()) {
                return min;
            }
            String val = sc.nextLine().trim();
            try {
                int num = Integer.parseInt(val);
                if (num >= min && num <= max) {
                    return num;
                }
                System.out.printf(PROMPT_INT_OUT_OF_RANGE, min, max);
            } catch (NumberFormatException e) {
                System.out.printf(PROMPT_INT_INVALID, min, max);
            }
        }
    }

    private static String normalizeGenderInput(String input) {
        if (input == null) {
            return EMPTY_STRING;
        }
        String clean = input.trim().toUpperCase();
        return switch (clean) {
            case OPTION_1, GENDER_M_SHORT, GENDER_MALE, GENDER_MASCULINO, GENDER_HOMEM -> GENDER_MALE;
            case OPTION_2, GENDER_F_SHORT, GENDER_FEMALE, GENDER_FEMININO, GENDER_MULHER -> GENDER_FEMALE;
            case OPTION_3, GENDER_U_SHORT, GENDER_UNISEX, GENDER_UNISSEXO -> GENDER_UNISEX;
            case OPTION_4, GENDER_A_SHORT, GENDER_ACCESSIBLE, GENDER_ACESSIVEL -> GENDER_ACCESSIBLE;
            default -> clean;
        };
    }

    // User command handlers

    private static void handleRegister(Scanner sc, FWCt system, String args) {
        String id = EMPTY_STRING;
        String roleStr = EMPTY_STRING;
        String password = EMPTY_STRING;
        String name = EMPTY_STRING;

        boolean isAdmin = system.hasActiveUser() && system.getCurrentUser().getRole() == Role.ADMIN;

        if (!args.isEmpty()) {
            String[] tokens = args.split(REGEX_WHITESPACE, 4);
            if (tokens.length >= 1) {
                id = tokens[0];
            }
            if (tokens.length >= 2) {
                if (ROLE_STUDENT.equalsIgnoreCase(tokens[1]) || ROLE_ADMIN.equalsIgnoreCase(tokens[1])) {
                    if (!ROLE_STUDENT.equalsIgnoreCase(tokens[1]) && !isAdmin) {
                        System.out.println(ADMIN_ONLY_ROLE_MSG);
                        return;
                    }
                    roleStr = tokens[1].toUpperCase();
                    if (tokens.length >= 3) {
                        password = tokens[2];
                    }
                    if (tokens.length >= 4) {
                        name = tokens[3];
                    }
                } else if (tokens.length == 2) {
                    name = tokens[1];
                    roleStr = ROLE_STUDENT;
                } else {
                    name = tokens[1];
                    password = tokens[2];
                    roleStr = ROLE_STUDENT;
                }
            }
        }

        if (id.isEmpty()) {
            id = promptNonEmpty(sc, PROMPT_USER_ID);
        }

        if (system.hasUser(id)) {
            System.out.printf(USER_ALREADY_EXISTS_MSG, id);
            return;
        }

        if (name.isEmpty()) {
            name = promptNonEmpty(sc, PROMPT_USER_NAME);
        }

        if (roleStr.isEmpty()) {
            if (isAdmin) {
                System.out.println(PROMPT_ADMIN_ROLE_MENU);
                String opt = promptNonEmpty(sc, PROMPT_ROLE_CHOICE);
                roleStr = switch (opt) {
                    case OPTION_1 -> ROLE_STUDENT;
                    case OPTION_2 -> ROLE_ADMIN;
                    default -> opt;
                };
            } else {
                roleStr = ROLE_STUDENT;
            }
        }

        if (password.isEmpty()) {
            password = promptNonEmpty(sc, PROMPT_PASSWORD);
            String confirmPassword = promptNonEmpty(sc, PROMPT_CONFIRM_PASSWORD);
            if (!password.equals(confirmPassword)) {
                System.out.println(PASSWORDS_MISMATCH_MSG);
                return;
            }
        }

        try {
            system.registerUser(id, name, roleStr, password);
            System.out.printf(USER_REGISTERED_MSG, id, name);
        } catch (UserAlreadyExistsException e) {
            System.out.printf(USER_ALREADY_EXISTS_MSG, id);
        } catch (IllegalArgumentException e) {
            System.out.println(INVALID_USER_DATA_MSG);
        }
    }

    private static void handleLogin(Scanner sc, FWCt system, String args) {
        String id = EMPTY_STRING;
        String password = EMPTY_STRING;

        if (!args.isEmpty()) {
            String[] tokens = args.split(REGEX_WHITESPACE, 2);
            if (tokens.length >= 1) id = tokens[0];
            if (tokens.length >= 2) password = tokens[1];
        }

        if (id.isEmpty()) {
            id = promptNonEmpty(sc, PROMPT_LOGIN_ID);
        }

        if (!system.hasUser(id)) {
            System.out.printf(USER_NOT_FOUND_MSG, id);
            return;
        }

        if (password.isEmpty()) {
            password = promptNonEmpty(sc, PROMPT_LOGIN_PASSWORD);
        }

        try {
            system.login(id, password);
            User user = system.getCurrentUser();
            System.out.printf(LOGIN_SUCCESS_MSG, user.getName(), user.getRole().getDescription());
        } catch (UserDoesNotExistException e) {
            System.out.printf(USER_NOT_FOUND_MSG, id);
        } catch (InvalidPasswordException e) {
            System.out.println(WRONG_PASSWORD_MSG);
        }
    }

    private static void handleLogout(FWCt system) {
        try {
            system.logout();
            System.out.println(LOGOUT_SUCCESS_MSG);
        } catch (NoActiveUserException e) {
            System.out.println(NO_USER_LOGGED_IN_MSG);
        }
    }

    private static void handleWhoAmI(FWCt system) {
        if (!system.hasActiveUser()) {
            System.out.println(NO_USER_LOGGED_IN_MSG);
            return;
        }
        User u = system.getCurrentUser();
        System.out.printf(WHOAMI_LAYOUT_MSG, u.getName(), u.getId(), u.getRole().getDescription());
    }

    private static void handleUsers(FWCt system) {
        if (!system.hasActiveUser()) {
            System.out.println(NO_ACTIVE_USER_MSG);
            return;
        }
        if (system.getCurrentUser().getRole() != Role.ADMIN) {
            System.out.println(ADMIN_ONLY_ACTION_MSG);
            return;
        }
        System.out.println(USER_LIST_HEADER_MSG);
        Iterator<User> it = system.listUsers();
        while (it.hasNext()) {
            User u = it.next();
            System.out.printf(USER_ENTRY_LAYOUT_MSG,
                    u.getId(), u.getName(), u.getRole().getDescription(),
                    u.getReviewCount(), u.getAlertCount());
        }
    }

    // Building command handlers

    private static void handleListBuildings(FWCt system) {
        System.out.println(BUILDING_LIST_HEADER_MSG);
        Iterator<Building> it = system.listBuildings();
        while (it.hasNext()) {
            Building b = it.next();
            System.out.printf(BUILDING_ENTRY_LAYOUT_MSG, b.getCode(), b.getName(), b.getBathroomCount());
        }
    }

    private static void handleAddBuilding(Scanner sc, FWCt system, String args) {
        if (!system.hasActiveUser()) {
            System.out.println(NO_ACTIVE_USER_MSG);
            return;
        }
        if (system.getCurrentUser().getRole() != Role.ADMIN) {
            System.out.println(ADMIN_ONLY_ACTION_MSG);
            return;
        }
        String code = EMPTY_STRING;
        String name = EMPTY_STRING;

        if (!args.isEmpty()) {
            String[] tokens = args.split(REGEX_WHITESPACE, 2);
            if (tokens.length >= 1) code = tokens[0];
            if (tokens.length >= 2) name = tokens[1];
        }

        if (code.isEmpty()) {
            code = promptNonEmpty(sc, PROMPT_BUILDING_CODE);
        }
        if (name.isEmpty()) {
            name = promptNonEmpty(sc, PROMPT_BUILDING_NAME);
        }

        try {
            system.registerBuilding(code, name);
            System.out.printf(BUILDING_ADDED_MSG, code, name);
        } catch (BuildingAlreadyExistsException e) {
            System.out.printf(BUILDING_ALREADY_EXISTS_MSG, code);
        } catch (IllegalArgumentException e) {
            System.out.println(BUILDING_REQUIRED_FIELDS_MSG);
        }
    }

    // Bathroom command handlers

    private static void handleAddBathroom(Scanner sc, FWCt system, String args) {
        if (!system.hasActiveUser()) {
            System.out.println(NO_ACTIVE_USER_MSG);
            return;
        }
        if (system.getCurrentUser().getRole() != Role.ADMIN) {
            System.out.println(ADMIN_ONLY_ACTION_MSG);
            return;
        }
        String id = EMPTY_STRING;
        String buildingCode = EMPTY_STRING;
        int floor = 0;
        String gender = EMPTY_STRING;
        String location = EMPTY_STRING;

        if (!args.isEmpty()) {
            String[] tokens = args.split(REGEX_WHITESPACE, 5);
            if (tokens.length >= 1) id = tokens[0];
            if (tokens.length >= 2) buildingCode = tokens[1];
            if (tokens.length >= 3) {
                try {
                    floor = Integer.parseInt(tokens[2]);
                } catch (NumberFormatException ignored) {}
            }
            if (tokens.length >= 4) gender = tokens[3];
            if (tokens.length >= 5) location = tokens[4];
        }

        if (id.isEmpty()) {
            id = promptNonEmpty(sc, PROMPT_BATHROOM_ID);
        }
        if (buildingCode.isEmpty()) {
            buildingCode = promptNonEmpty(sc, PROMPT_BATHROOM_BUILDING);
        }
        if (gender.isEmpty()) {
            floor = promptInt(sc, PROMPT_BATHROOM_FLOOR, 0, 10);
            gender = promptNonEmpty(sc, PROMPT_BATHROOM_GENDER);
        }
        if (location.isEmpty()) {
            location = promptOptional(sc, PROMPT_BATHROOM_LOCATION);
        }

        try {
            system.addStandardBathroom(id, buildingCode, floor, gender, location);
            System.out.printf(BATHROOM_ADDED_MSG, id.toUpperCase(), buildingCode.toUpperCase(), floor);
        } catch (BathroomAlreadyExistsException e) {
            System.out.printf(BATHROOM_ALREADY_EXISTS_MSG, id);
        } catch (BuildingDoesNotExistException e) {
            System.out.printf(BUILDING_NOT_FOUND_MSG, buildingCode);
        }
    }

    private static void handleAddAccessible(Scanner sc, FWCt system, String args) {
        if (!system.hasActiveUser()) {
            System.out.println(NO_ACTIVE_USER_MSG);
            return;
        }
        if (system.getCurrentUser().getRole() != Role.ADMIN) {
            System.out.println(ADMIN_ONLY_ACTION_MSG);
            return;
        }
        String id = EMPTY_STRING;
        String buildingCode = EMPTY_STRING;
        int floor = 0;
        String gender = EMPTY_STRING;
        String location = EMPTY_STRING;

        if (!args.isEmpty()) {
            String[] tokens = args.split(REGEX_WHITESPACE, 5);
            if (tokens.length >= 1) id = tokens[0];
            if (tokens.length >= 2) buildingCode = tokens[1];
            if (tokens.length >= 3) {
                try {
                    floor = Integer.parseInt(tokens[2]);
                } catch (NumberFormatException ignored) {}
            }
            if (tokens.length >= 4) gender = tokens[3];
            if (tokens.length >= 5) location = tokens[4];
        }

        if (id.isEmpty()) {
            id = promptNonEmpty(sc, PROMPT_ACCESSIBLE_ID);
        }
        if (buildingCode.isEmpty()) {
            buildingCode = promptNonEmpty(sc, PROMPT_BATHROOM_BUILDING);
        }
        if (gender.isEmpty()) {
            floor = promptInt(sc, PROMPT_BATHROOM_FLOOR, 0, 10);
            gender = promptNonEmpty(sc, PROMPT_ACCESSIBLE_GENDER);
        }
        if (location.isEmpty()) {
            location = promptOptional(sc, PROMPT_BATHROOM_LOCATION);
        }

        try {
            system.addAccessibleBathroom(id, buildingCode, floor, gender, location, true, true);
            System.out.printf(BATHROOM_ADDED_MSG, id.toUpperCase(), buildingCode.toUpperCase(), floor);
        } catch (BathroomAlreadyExistsException e) {
            System.out.printf(BATHROOM_ALREADY_EXISTS_MSG, id);
        } catch (BuildingDoesNotExistException e) {
            System.out.printf(BUILDING_NOT_FOUND_MSG, buildingCode);
        }
    }

    // Search and discovery handlers

    private static void handleFindBest(Scanner sc, FWCt system, String args) {
        // Direct bathroom ID shortcut (e.g. "best di-0-m1")
        if (!args.isEmpty()) {
            String firstToken = args.split(REGEX_WHITESPACE)[0];
            if (system.hasBathroom(firstToken)) {
                handleBathroomDetails(sc, system, firstToken);
                return;
            }
        }

        String buildingCode = EMPTY_STRING;
        Integer floorFilter = null;
        String genderFilter = EMPTY_STRING;

        if (!args.isEmpty()) {
            String[] tokens = args.split(REGEX_WHITESPACE);
            if (tokens.length >= 1) {
                buildingCode = tokens[0];
            }
            if (tokens.length >= 2) {
                try {
                    floorFilter = Integer.parseInt(tokens[1]);
                    if (tokens.length >= 3) {
                        genderFilter = normalizeGenderInput(tokens[2]);
                    }
                } catch (NumberFormatException e) {
                    genderFilter = normalizeGenderInput(tokens[1]);
                }
            }
        }

        if (buildingCode.isEmpty()) {
            buildingCode = promptNonEmpty(sc, PROMPT_SEARCH_BUILDING);
        }

        if (!system.hasBuilding(buildingCode)) {
            System.out.printf(BUILDING_NOT_FOUND_MSG, buildingCode);
            return;
        }

        Building building;
        try {
            building = system.getBuilding(buildingCode);
        } catch (BuildingDoesNotExistException e) {
            System.out.printf(BUILDING_NOT_FOUND_MSG, buildingCode);
            return;
        }

        // Ask for floor if not provided in arguments and running interactively
        if (floorFilter == null && (args.isEmpty() || args.split(REGEX_WHITESPACE).length <= 1)) {
            String floorInput = promptOptional(sc, PROMPT_SEARCH_FLOOR);
            if (!floorInput.isEmpty()) {
                try {
                    floorFilter = Integer.parseInt(floorInput);
                } catch (NumberFormatException ignored) {}
            }
        }

        // Ask for gender if not provided in arguments and running interactively
        if (genderFilter.isEmpty() && (args.isEmpty() || args.split(REGEX_WHITESPACE).length <= 1)) {
            System.out.println(PROMPT_SEARCH_GENDER_MENU);
            String opt = promptOptional(sc, PROMPT_SEARCH_GENDER_CHOICE);
            genderFilter = normalizeGenderInput(opt);
        }

        // Find and rank all operational bathrooms matching the search criteria
        List<Bathroom> operationalMatches = new ArrayList<>();
        Iterator<Bathroom> it = building.getBathrooms();
        while (it.hasNext()) {
            Bathroom b = it.next();
            if (!b.isOperational()) {
                continue;
            }
            if (floorFilter != null && b.getFloor() != floorFilter) {
                continue;
            }
            if (!genderFilter.isEmpty() && !b.getGender().equalsIgnoreCase(genderFilter)
                    && !b.getGender().equalsIgnoreCase(GENDER_UNISEX)) {
                continue;
            }
            operationalMatches.add(b);
        }

        if (operationalMatches.isEmpty()) {
            System.out.printf(SEARCH_NOT_FOUND_LAYOUT, building.getName());
            return;
        }

        for (int i = 0; i < operationalMatches.size() - 1; i++) {
            for (int j = 0; j < operationalMatches.size() - i - 1; j++) {
                Bathroom b1 = operationalMatches.get(j);
                Bathroom b2 = operationalMatches.get(j + 1);
                int cmp = Double.compare(b1.getAverageRating(), b2.getAverageRating());
                if (cmp == 0) {
                    cmp = Integer.compare(b1.getReviewCount(), b2.getReviewCount());
                }
                if (cmp < 0) {
                    operationalMatches.set(j, b2);
                    operationalMatches.set(j + 1, b1);
                }
            }
        }

        Bathroom best = operationalMatches.get(0);
        String floorInfo = EMPTY_STRING;
        if (floorFilter != null) {
            floorInfo = String.format(FLOOR_FILTER_FORMAT, floorFilter);
        }

        System.out.println(BANNER_BORDER);
        System.out.printf(SEARCH_AVAILABLE_HEADER_LAYOUT, building.getName(), floorInfo);
        System.out.println(BANNER_BORDER);

        int index = 1;
        for (Bathroom b : operationalMatches) {
            String loc = b.getLocation();
            if (loc.isEmpty()) {
                loc = FLOOR_PREFIX + b.getFloor();
            }
            String badge = EMPTY_STRING;
            if (b == best) {
                badge = BEST_BADGE;
            }
            String ratingStr = formatRating(b.getAverageRating(), b.getReviewCount());
            System.out.printf(SEARCH_ENTRY_LAYOUT, index++, b.getId(), loc, b.getGender(), ratingStr, badge);
        }

        System.out.println(BANNER_BORDER);
        System.out.printf(BEST_CARD_DIRECT_HEADER_LAYOUT, best.getId(), best.getGender());
        System.out.println(BANNER_BORDER);
        String locStr = EMPTY_STRING;
        if (!best.getLocation().isEmpty()) {
            locStr = String.format(LOCATION_PAREN_FORMAT, best.getLocation());
        }
        System.out.printf(CARD_FIELD_LOCATION_FULL_LAYOUT, building.getName(), best.getFloor(), locStr);
        System.out.printf(CARD_FIELD_STATUS_LAYOUT, formatStatusLabel(best.getStatus()));
        System.out.printf(CARD_FIELD_RATING_LAYOUT, formatRating(best.getAverageRating(), best.getReviewCount()));

        Iterator<Alert> alerts = best.getActiveAlerts();
        if (alerts.hasNext()) {
            System.out.print(CARD_ALERTS_HEADER);
            while (alerts.hasNext()) {
                Alert a = alerts.next();
                System.out.printf(CARD_ALERT_ITEM_LAYOUT, a.getDescription());
            }
        } else {
            System.out.print(CARD_ALERTS_EMPTY);
        }
        System.out.println(BANNER_BORDER);
    }

    private static void handleBathroomDetails(Scanner sc, FWCt system, String args) {
        String id;
        if (args.isEmpty()) {
            id = promptNonEmpty(sc, PROMPT_BATHROOM_ID);
        } else {
            id = args;
        }

        if (!system.hasBathroom(id)) {
            System.out.printf(BATHROOM_NOT_FOUND_MSG, id);
            return;
        }

        try {
            Bathroom b = system.getBathroom(id);
            Building building = system.getBuilding(b.getBuildingCode());
            String locStr = EMPTY_STRING;
            if (!b.getLocation().isEmpty()) {
                locStr = String.format(LOCATION_PAREN_FORMAT, b.getLocation());
            }

            System.out.println(BANNER_BORDER);
            System.out.printf(CARD_DETAIL_HEADER_LAYOUT, b.getId(), building.getName(), b.getFloor(), locStr);
            System.out.println(BANNER_BORDER);
            String accessibleBadge = EMPTY_STRING;
            if (b.isAccessible()) {
                accessibleBadge = ACCESSIBLE_BADGE_LABEL;
            }
            System.out.printf(CARD_FIELD_TYPE_LAYOUT, b.getGender(), accessibleBadge);
            System.out.printf(CARD_FIELD_STATUS_LAYOUT, formatStatusLabel(b.getStatus()));
            System.out.printf(CARD_FIELD_RATING_LAYOUT, formatRating(b.getAverageRating(), b.getReviewCount()));

            Iterator<Alert> alerts = b.getActiveAlerts();
            if (!alerts.hasNext()) {
                System.out.print(CARD_ALERTS_EMPTY);
            } else {
                System.out.print(CARD_ALERTS_HEADER);
                while (alerts.hasNext()) {
                    Alert a = alerts.next();
                    String statusText;
                    if (a.isConfirmed()) {
                        statusText = ALERT_CONFIRMED_STATUS_LABEL;
                    } else {
                        statusText = String.format(ALERT_PENDING_STATUS_LAYOUT, a.getConfirmationCount(), a.getRequiredConfirmations());
                    }
                    System.out.printf(CARD_ALERT_DETAIL_ITEM_LAYOUT, a.getDescription(), statusText);
                }
            }

            Iterator<Review> reviews = b.getReviews();
            if (reviews.hasNext()) {
                System.out.print(CARD_REVIEWS_HEADER);
                int count = 0;
                while (reviews.hasNext() && count < 3) {
                    Review r = reviews.next();
                    System.out.printf(CARD_REVIEW_ITEM_LAYOUT, r.getAuthorName(), r.getRating(), r.getComment());
                    count++;
                }
            }
            System.out.println(BANNER_BORDER);
        } catch (BathroomDoesNotExistException | BuildingDoesNotExistException ignored) {
            System.out.printf(BATHROOM_NOT_FOUND_MSG, id);
        }
    }

    private static void handleListAll(FWCt system) {
        System.out.println(LIST_ALL_HEADER);
        Iterator<Bathroom> it = system.listAllBathrooms();
        printBathroomList(it, system);
    }



    // Review command handlers

    private static void handleAddReview(Scanner sc, FWCt system, String args) {
        if (!system.hasActiveUser()) {
            System.out.println(NO_ACTIVE_USER_MSG);
            return;
        }

        String bathroomId = EMPTY_STRING;
        int rating = -1;
        String comment = EMPTY_STRING;

        if (!args.isEmpty()) {
            String[] tokens = args.split(REGEX_WHITESPACE, 3);
            if (tokens.length >= 1) {
                bathroomId = tokens[0];
            }
            if (tokens.length >= 2) {
                try {
                    rating = Integer.parseInt(tokens[1]);
                } catch (NumberFormatException ignored) {}
            }
            if (tokens.length >= 3) {
                comment = tokens[2].trim();
            }
        }

        if (bathroomId.isEmpty()) {
            bathroomId = promptNonEmpty(sc, PROMPT_BATHROOM_ID);
        }

        if (!system.hasBathroom(bathroomId)) {
            System.out.printf(BATHROOM_NOT_FOUND_MSG, bathroomId);
            return;
        }

        if (rating < 0 || rating > 5) {
            rating = promptInt(sc, PROMPT_REVIEW_RATING, 1, 5);
        }

        if (comment.isEmpty()) {
            comment = promptOptional(sc, PROMPT_REVIEW_COMMENT);
            if (comment.isEmpty()) {
                comment = DEFAULT_COMMENT;
            }
        }

        try {
            system.addReview(bathroomId, rating, comment);
            System.out.printf(REVIEW_ADDED_MSG, bathroomId.toUpperCase());
        } catch (NoActiveUserException e) {
            System.out.println(NO_ACTIVE_USER_MSG);
        } catch (BathroomDoesNotExistException e) {
            System.out.printf(BATHROOM_NOT_FOUND_MSG, bathroomId);
        } catch (InvalidRatingException e) {
            System.out.println(INVALID_RATING_MSG);
        } catch (DuplicateReviewInContestException e) {
            System.out.printf(DUPLICATE_REVIEW_MSG, system.getCurrentContestWeek());
        }
    }



    // Alert command handlers

    private static void handleAddAlert(Scanner sc, FWCt system, String args) {
        if (!system.hasActiveUser()) {
            System.out.println(NO_ACTIVE_USER_MSG);
            return;
        }

        String bathroomId = EMPTY_STRING;
        String typeStr = EMPTY_STRING;
        String description = EMPTY_STRING;

        if (!args.isEmpty()) {
            String[] tokens = args.split(REGEX_WHITESPACE, 3);
            if (tokens.length >= 1) bathroomId = tokens[0];
            if (tokens.length >= 2) typeStr = tokens[1];
            if (tokens.length >= 3) description = tokens[2];
        }

        if (bathroomId.isEmpty()) {
            bathroomId = promptNonEmpty(sc, PROMPT_BATHROOM_ID);
        }

        if (!system.hasBathroom(bathroomId)) {
            System.out.printf(BATHROOM_NOT_FOUND_MSG, bathroomId);
            return;
        }

        if (typeStr.isEmpty()) {
            System.out.println(PROMPT_ALERT_TYPE_MENU);
            String opt = promptNonEmpty(sc, PROMPT_ALERT_TYPE_CHOICE);
            typeStr = switch (opt) {
                case OPTION_1 -> ALERT_TYPE_CLEANING;
                case OPTION_2 -> ALERT_TYPE_MAINTENANCE;
                default -> opt;
            };
        }

        if (description.isEmpty()) {
            if (typeStr.equalsIgnoreCase(ALERT_TYPE_CLEANING)) {
                description = ALERT_CLEANING_DEFAULT_DESC;
            } else {
                description = ALERT_MAINTENANCE_DEFAULT_DESC;
            }
        }

        try {
            Alert alert = system.addAlert(bathroomId, typeStr, description);
            if (alert.isConfirmed()) {
                System.out.printf(ALERT_CREATED_MSG, bathroomId.toUpperCase(), alert.getDescription());
            } else {
                System.out.printf(ALERT_PENDING_MSG, bathroomId.toUpperCase());
            }
        } catch (NoActiveUserException e) {
            System.out.println(NO_ACTIVE_USER_MSG);
        } catch (BathroomDoesNotExistException e) {
            System.out.printf(BATHROOM_NOT_FOUND_MSG, bathroomId);
        }
    }

    private static void handleConfirmAlert(Scanner sc, FWCt system, String args) {
        String idOrBathroom;
        if (args.isEmpty()) {
            idOrBathroom = promptNonEmpty(sc, PROMPT_CONFIRM_BATHROOM);
        } else {
            idOrBathroom = args;
        }

        try {
            boolean newlyConfirmed = system.confirmAlert(idOrBathroom);
            if (newlyConfirmed) {
                System.out.printf(ALERT_CONFIRMED_SUCCESS_MSG, idOrBathroom.toUpperCase());
            } else {
                System.out.printf(CONFIRM_RECORDED_MSG, idOrBathroom.toUpperCase());
            }
        } catch (NoActiveUserException e) {
            System.out.println(NO_ACTIVE_USER_MSG);
        } catch (AlertNotFoundException e) {
            System.out.printf(ALERT_NOT_FOUND_MSG, idOrBathroom.toUpperCase());
        } catch (AlertAlreadyConfirmedException e) {
            System.out.println(ALERT_ALREADY_CONFIRMED_MSG);
        }
    }

    private static void handleResolveAlert(Scanner sc, FWCt system, String args) {
        if (!system.hasActiveUser()) {
            System.out.println(NO_ACTIVE_USER_MSG);
            return;
        }
        User user = system.getCurrentUser();
        if (user.getRole() != Role.ADMIN) {
            System.out.println(ADMIN_ONLY_REOPEN_MSG);
            return;
        }

        String idOrBathroom;
        if (args.isEmpty()) {
            idOrBathroom = promptNonEmpty(sc, PROMPT_REOPEN_BATHROOM);
        } else {
            idOrBathroom = args;
        }

        try {
            boolean fullyResolved = system.resolveAlert(idOrBathroom);
            if (fullyResolved) {
                System.out.printf(ALERT_RESOLVED_MSG, idOrBathroom.toUpperCase());
            } else {
                System.out.printf(RESOLUTION_VOTE_MSG, idOrBathroom.toUpperCase());
            }
        } catch (NoActiveUserException e) {
            System.out.println(NO_ACTIVE_USER_MSG);
        } catch (AlertNotFoundException e) {
            System.out.printf(ALERT_NOT_FOUND_MSG, idOrBathroom.toUpperCase());
        } catch (AlertAlreadyResolvedException e) {
            System.out.printf(ALERT_ALREADY_RESOLVED_MSG, idOrBathroom.toUpperCase());
        }
    }

    private static void handleListAlerts(FWCt system) {
        Iterator<Alert> it = system.listActiveAlerts();
        if (!it.hasNext()) {
            System.out.println(NO_ACTIVE_ALERTS_MSG);
            return;
        }
        System.out.println(ALERTS_HEADER_MSG);
        while (it.hasNext()) {
            Alert a = it.next();
            String status;
            if (a.isConfirmed()) {
                status = ALERT_CONFIRMED_STATUS_LABEL;
            } else {
                status = String.format(ALERT_PENDING_STATUS_LAYOUT, a.getConfirmationCount(), a.getRequiredConfirmations());
            }
            System.out.printf(ALERT_CAMPUS_ROW_LAYOUT, a.getBathroomId(), a.getDescription(), status);
        }
    }

    // Contest command handlers

    private static void handleContestStatus(FWCt system) {
        System.out.printf(CONTEST_HEADER_LAYOUT, system.getCurrentContestWeek());
        Iterator<Bathroom> it = system.getTopRatedBathrooms(3);
        int rank = 1;
        boolean hasAny = false;
        while (it.hasNext()) {
            Bathroom b = it.next();
            if (b.getReviewCount() > 0) {
                hasAny = true;
                String locStr = EMPTY_STRING;
                if (!b.getLocation().isEmpty()) {
                    locStr = String.format(LOCATION_PAREN_FORMAT, b.getLocation());
                }
                System.out.printf(CONTEST_ROW_LAYOUT,
                        rank++, b.getId(), b.getBuildingCode(), locStr,
                        formatRating(b.getAverageRating(), b.getReviewCount()));
            }
        }
        if (!hasAny) {
            System.out.println(CONTEST_EMPTY_MSG);
        }
    }

    private static void handleAdvanceContest(FWCt system) {
        if (!system.hasActiveUser()) {
            System.out.println(NO_ACTIVE_USER_MSG);
            return;
        }
        if (system.getCurrentUser().getRole() != Role.ADMIN) {
            System.out.println(ADMIN_ONLY_ACTION_MSG);
            return;
        }
        system.advanceContestWeek();
        System.out.printf(CONTEST_ADVANCED_LAYOUT, system.getCurrentContestWeek());
    }

    private static void handleHelp() {
        System.out.println(HELP_CONTENT);
    }

    // Formatting helpers

    private static void printBathroomList(Iterator<Bathroom> it, FWCt system) {
        boolean found = false;
        while (it.hasNext()) {
            found = true;
            Bathroom b = it.next();
            String accessibleBadge = EMPTY_STRING;
            if (b.isAccessible()) {
                accessibleBadge = ACCESSIBLE_BADGE_LABEL;
            }
            String locStr = EMPTY_STRING;
            if (!b.getLocation().isEmpty()) {
                locStr = String.format(LOCATION_PAREN_FORMAT, b.getLocation());
            }
            System.out.printf(BATHROOM_ROW_LAYOUT,
                    b.getId(), b.getBuildingCode(), b.getFloor(), locStr, b.getGender(),
                    accessibleBadge, formatStatusLabel(b.getStatus()),
                    formatRating(b.getAverageRating(), b.getReviewCount()));
        }
        if (!found) {
            System.out.println(NO_BATHROOMS_FOUND_MSG);
        }
    }

    private static String formatRating(double rating, int count) {
        if (count == 0) {
            return RATING_EMPTY_LABEL;
        }
        int intRating = (int) rating;
        String score;
        if (rating == intRating) {
            score = String.format("%d", intRating);
        } else {
            score = String.format(SCORE_DECIMAL_FORMAT, rating);
        }
        String countLabel;
        if (count == 1) {
            countLabel = RATING_COUNT_SINGLE_LABEL;
        } else {
            countLabel = RATING_COUNT_PLURAL_LABEL;
        }
        return String.format(RATING_COUNT_TEMPLATE, score, count, countLabel);
    }

    private static String formatStatusLabel(BathroomStatus status) {
        return switch (status) {
            case AVAILABLE -> STATUS_AVAILABLE_LABEL;
            case CLEANING -> STATUS_CLEANING_LABEL;
            case MAINTENANCE -> STATUS_MAINTENANCE_LABEL;
        };
    }
}
