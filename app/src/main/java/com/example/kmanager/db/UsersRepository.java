package com.example.kmanager.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kmanager.db.dao.UserDAO;
import com.example.kmanager.db.entity.UserEntity;

import java.util.List;

public class UsersRepository {

    private UserDAO userDao;
    private LiveData<List<UserEntity>> mAllUsers;

    public UsersRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDAO();
        mAllUsers = userDao.getAllUsers();
    }

    public LiveData<List<UserEntity>> getAllUsers() {
        return mAllUsers;
    }

    public UserEntity getUser(String username) {
//        return new getUserAsync(userDao).execute(username).get();
        return userDao.getUserByUsername(username);
    }

    public UserEntity getUserLogin(String username, String password) {
        return userDao.getUserLogin(username, password);
    }

    public long insertUser(UserEntity user) {
//        new insertUsersAsync(userDao).execute(user);
        return userDao.insert(user);
    }

    public void updateUser(UserEntity user) {
//        new updateUsersAsync(userDao).execute(user);
    }

    public void deleteUser(UserEntity user) {
//        new deleteUsersAsync(userDao).execute(user);
    }

    public void deleteAllUsers() {
//        new deleteAllUsersAsync(userDao).execute();
    }

//    /**
//     * NOTE: all write operations should be done in background thread,
//     * otherwise the following error will be thrown
//     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
//     */
//
//    private static class getUserAsync extends AsyncTask<String, Void, UserEntity> {
//
//        private UserDAO userDAOAsync;
//
//        getUserAsync(UserDAO animalDao) {
//            userDAOAsync = animalDao;
//        }
//
//        @Override
//        protected UserEntity doInBackground(String... ids) {
//            return userDAOAsync.getUserByUsername(ids[0]);
//        }
//    }
//
//    private static class insertUsersAsync extends AsyncTask<UserEntity, Void, Long> {
//
//        private UserDAO userDAOAsync;
//
//        insertUsersAsync(UserDAO user) {
//            userDAOAsync = user;
//        }
//
//        @Override
//        protected Long doInBackground(UserEntity... users) {
//            long id = userDAOAsync.insert(users[0]);
//            return id;
//        }
//    }
//
//    private static class updateUsersAsync extends AsyncTask<UserEntity, Void, Void> {
//
//        private UserDAO userDAOAsync;
//
//        updateUsersAsync(UserDAO userDao) {
//            userDAOAsync = userDao;
//        }
//
//        @Override
//        protected Void doInBackground(UserEntity... users) {
//            userDAOAsync.update(users[0]);
//            return null;
//        }
//    }
//
//    private static class deleteUsersAsync extends AsyncTask<UserEntity, Void, Void> {
//
//        private UserDAO userDAOAsync;
//
//        deleteUsersAsync(UserDAO userDao) {
//            userDAOAsync = userDao;
//        }
//
//        @Override
//        protected Void doInBackground(UserEntity... users) {
//            userDAOAsync.delete(users[0]);
//            return null;
//        }
//    }
//
//    private static class deleteAllUsersAsync extends AsyncTask<UserEntity, Void, Void> {
//
//        private UserDAO userDAOAsync;
//
//        deleteAllUsersAsync(UserDAO userDao) {
//            userDAOAsync = userDao;
//        }
//
//        @Override
//        protected Void doInBackground(UserEntity... users) {
//            userDAOAsync.deleteAll();
//            return null;
//        }
//    }
}