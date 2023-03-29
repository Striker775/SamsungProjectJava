package com.example.samsungprojectjava;
//Класс данных о пользователе
public class LoginClass {
    long id;
    String login;
    String password;
    int remember;
    int low_tasks;
    int medium_tasks;
    int high_tasks;
    LoginClass(long id, String login, String password, int remember, int low, int medium, int high){
        this.id = id;
        this.login = login;
        this.password = password;
        this.remember = remember;
        this.low_tasks = low;
        this.medium_tasks = medium;
        this.high_tasks = high;
    }
}
