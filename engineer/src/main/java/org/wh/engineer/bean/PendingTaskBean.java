package org.wh.engineer.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 钱凯
 * @创建时间: 2018/10/31
 * @功能描述:
 */
public class PendingTaskBean {

    /**
     * id : 0
     * accountId : 8a80cb8164d9b3940164da1bff760005
     * userId : 8a80cb8164d9b3940164da1bff760003
     * messages : [{"id":"4028829366f140c10166f144aaea0000","type":"1","text":"罗丹贝尔-reader,柜子,网络多功能集控设备","detail":"{'receiveOrderId':'40288293667aa53e01667aa5fc610016'}","createTime":"2018-11-08 11:01:41","accountId":"8a80cb8164d9b3940164da1bff760005","title":"医嘱单未领用"},{"id":"4028829366f1d03d0166f1d1bd4c0000","type":"1","text":"罗丹贝尔-reader,柜子,网络多功能集控设备","detail":"{'receiveOrderId':'40288293667aa53e01667aa5fc610016'}","createTime":"2018-11-08 13:35:47","accountId":"8a80cb8164d9b3940164da1bff760005","title":"医嘱单未领用"}]
     */

    private int id;
    private String accountId;
    private String userId;
    private List<MessagesBean> messages = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<MessagesBean> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesBean> messages) {
        this.messages = messages;
    }

    public static class MessagesBean {
        /**
         * id : 4028829366f140c10166f144aaea0000
         * type : 1
         * text : 罗丹贝尔-reader,柜子,网络多功能集控设备
         * detail : {'receiveOrderId':'40288293667aa53e01667aa5fc610016'}
         * createTime : 2018-11-08 11:01:41
         * accountId : 8a80cb8164d9b3940164da1bff760005
         * title : 医嘱单未领用
         */

        private String messageId;
        private String type;
        private String text;
        private String detail;
        private String createTime;
        private String accountId;
        private String title;

        public String getmessageId() {
            return messageId;
        }

        public void setmessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
