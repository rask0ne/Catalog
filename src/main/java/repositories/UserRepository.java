package repositories;

import models.UsersEntity;

/**
 * Created by rask on 03.03.2017.
 */

/**
 * User singleton class to keep information during programm work.
 */
public class UserRepository {

    private static UserRepository instance = null;

    //public UsersEntity user = new UsersEntity();

    public UserRepository() {};

    private String username;
    private int role;
    private int userId;

    public static final UserRepository getInstance(){

        if(instance == null){
             instance = new UserRepository();

            }
        return instance;
    }

    public int getId(){

        return userId;

    }

    public int getRole(){

        return role;

    }

    public String getName(){

        return username;

    }

    public void setId(int id){

        this.userId = id;

    }

    public void setRole(int role){

        this.role = role;

    }

    public void setUsername(String name){

        this.username = name;

    }

}
