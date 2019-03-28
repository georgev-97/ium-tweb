/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lorenzo
 */
public class User {

        private String id;
        private String account;
        private int role;

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public User(String id, String account, int role) {
            this.id = id;
            this.account = account;
            this.role = role;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
