package hibernate.Util;

/**
 * Created by rask on 04.03.2017.
 */
public enum UserRoles {

    GUEST, USER, ADMIN;

    public static String getUserRole(UserRoles role){
        switch (role){
            case GUEST : return "GUEST";
            case USER : return "USER";
            case ADMIN : return "ADMIN";
            default : return null;
        }
    }

    }
