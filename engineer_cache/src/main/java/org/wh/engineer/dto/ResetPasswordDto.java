package org.wh.engineer.dto;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/3
 * @功能描述:
 */
public class ResetPasswordDto {

    /**
     * account : {"password":"21212","accountName":"adminUM"}
     */

    private AccountBean account;

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public static class AccountBean {
        /**
         * password : 21212
         * accountName : adminUM
         */

        private String password;
        private String accountName;
        private String accountId;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }
    }
}
