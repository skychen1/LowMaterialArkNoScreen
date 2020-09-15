package org.wh.engineer.dto;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/2
 * @功能描述:获取用户信息dto
 */
public class GetAccountInfoDto {


    /**
     * account : {"accountId":"8a80cb8164d9b3940164da1bff760002"}
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
         * accountId : 8a80cb8164d9b3940164da1bff760002
         */

        private String accountId;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }
    }
}
