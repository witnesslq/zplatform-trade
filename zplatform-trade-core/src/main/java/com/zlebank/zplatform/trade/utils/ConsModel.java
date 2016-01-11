package com.zlebank.zplatform.trade.utils;

public class ConsModel {
	private String zlrt_url="";
	private String instuId="";
	private String keyStorePath = "";
	private String cerPath = "";
	
	private String keyStorePath_zlrt = "";
	private String cerPath_zlrt = "";
	
	private String sendMarginSms_url = "";
	private String marginRegisterReq_url="";
	private String onlineDepositShort_url="";
	private String offlineDepositNotifyReq_url="";
	private String withdrawNotifyReq_url="";
	private String querytranstatus_url="";
	
	private String rout_switch="";
	private String eciti_chnl_code="";
	private String eciti_insti_code="";
	private String eciti_safe_no="";
	private String zlpay_chnl_code="";
	private String zlpay_chnl_merch_name="";
	
	private String reapay_quickpay_version="";
	private String reapay_quickpay_url="";
	private String reapay_quickpay_debit_url="";
	private String reapay_quickpay_merchant_id="";
	private String reapay_quickpay_seller_email="";
	private String reapay_quickpay_notify_url="";
	private String reapay_quickpay_key="";
	private String reapay_quickpay_pubkey="";
	private String reapay_quickpay_prikey="";
	private String reapay_quickpay_sms_url = "";
	private String reapay_quickpay_credit_url="";
	private String reapay_quickpay_pay_url="";
	private String reapay_quickpay_prikey_pwd="";
	private String reapay_quickpay_query_url="";
	private String reapay_chnl_code="";
	private String reapay_quickpay_bind_url="";
	/**会员支付密码MD5生成用的KEY**/
	private String member_pay_password_key="";
	
	private String order_number_file_path="";
	public String getZlrt_url() {
		return zlrt_url;
	}
	
	public void setZlrt_url(String zlrtUrl) {
		zlrt_url = zlrtUrl;
	}
	
	public String getInstuId() {
		return instuId;
	}
	
	public void setInstuId(String instuId) {
		this.instuId = instuId;
	}

	/**
	 * @return the keyStorePath
	 */
	public String getKeyStorePath() {
		return keyStorePath;
	}

	/**
	 * @param keyStorePath the keyStorePath to set
	 */
	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	/**
	 * @return the cerPath
	 */
	public String getCerPath() {
		return cerPath;
	}

	/**
	 * @param cerPath the cerPath to set
	 */
	public void setCerPath(String cerPath) {
		this.cerPath = cerPath;
	}

	/**
	 * @return the keyStorePath_zlrt
	 */
	public String getKeyStorePath_zlrt() {
		return keyStorePath_zlrt;
	}

	/**
	 * @param keyStorePathZlrt the keyStorePath_zlrt to set
	 */
	public void setKeyStorePath_zlrt(String keyStorePathZlrt) {
		keyStorePath_zlrt = keyStorePathZlrt;
	}

	/**
	 * @return the cerPath_zlrt
	 */
	public String getCerPath_zlrt() {
		return cerPath_zlrt;
	}

	/**
	 * @param cerPathZlrt the cerPath_zlrt to set
	 */
	public void setCerPath_zlrt(String cerPathZlrt) {
		cerPath_zlrt = cerPathZlrt;
	}

	/**
	 * @return the sendMarginSms_url
	 */
	public String getSendMarginSms_url() {
		return sendMarginSms_url;
	}

	/**
	 * @param sendMarginSmsUrl the sendMarginSms_url to set
	 */
	public void setSendMarginSms_url(String sendMarginSmsUrl) {
		sendMarginSms_url = sendMarginSmsUrl;
	}

	/**
	 * @return the marginRegisterReq_url
	 */
	public String getMarginRegisterReq_url() {
		return marginRegisterReq_url;
	}

	/**
	 * @param marginRegisterReqUrl the marginRegisterReq_url to set
	 */
	public void setMarginRegisterReq_url(String marginRegisterReqUrl) {
		marginRegisterReq_url = marginRegisterReqUrl;
	}

	/**
	 * @return the onlineDepositShort_url
	 */
	public String getOnlineDepositShort_url() {
		return onlineDepositShort_url;
	}

	/**
	 * @param onlineDepositShortUrl the onlineDepositShort_url to set
	 */
	public void setOnlineDepositShort_url(String onlineDepositShortUrl) {
		onlineDepositShort_url = onlineDepositShortUrl;
	}

	/**
	 * @return the offlineDepositNotifyReq_url
	 */
	public String getOfflineDepositNotifyReq_url() {
		return offlineDepositNotifyReq_url;
	}

	/**
	 * @param offlineDepositNotifyReqUrl the offlineDepositNotifyReq_url to set
	 */
	public void setOfflineDepositNotifyReq_url(String offlineDepositNotifyReqUrl) {
		offlineDepositNotifyReq_url = offlineDepositNotifyReqUrl;
	}

	/**
	 * @return the withdrawNotifyReq_url
	 */
	public String getWithdrawNotifyReq_url() {
		return withdrawNotifyReq_url;
	}

	/**
	 * @param withdrawNotifyReqUrl the withdrawNotifyReq_url to set
	 */
	public void setWithdrawNotifyReq_url(String withdrawNotifyReqUrl) {
		withdrawNotifyReq_url = withdrawNotifyReqUrl;
	}

	/**
	 * @return the querytranstatus_url
	 */
	public String getQuerytranstatus_url() {
		return querytranstatus_url;
	}

	/**
	 * @param querytranstatusUrl the querytranstatus_url to set
	 */
	public void setQuerytranstatus_url(String querytranstatusUrl) {
		querytranstatus_url = querytranstatusUrl;
	}

	/**
	 * @return the rout_switch
	 */
	public String getRout_switch() {
		return rout_switch;
	}

	/**
	 * @param routSwitch the rout_switch to set
	 */
	public void setRout_switch(String routSwitch) {
		rout_switch = routSwitch;
	}

    /**
     * @return the eciti_chnl_code
     */
    public String getEciti_chnl_code() {
        return eciti_chnl_code;
    }

    /**
     * @param eciti_chnl_code the eciti_chnl_code to set
     */
    public void setEciti_chnl_code(String eciti_chnl_code) {
        this.eciti_chnl_code = eciti_chnl_code;
    }

    /**
     * @return the eciti_insti_code
     */
    public String getEciti_insti_code() {
        return eciti_insti_code;
    }

    /**
     * @param eciti_insti_code the eciti_insti_code to set
     */
    public void setEciti_insti_code(String eciti_insti_code) {
        this.eciti_insti_code = eciti_insti_code;
    }

    /**
     * @return the eciti_safe_no
     */
    public String getEciti_safe_no() {
        return eciti_safe_no;
    }

    /**
     * @param eciti_safe_no the eciti_safe_no to set
     */
    public void setEciti_safe_no(String eciti_safe_no) {
        this.eciti_safe_no = eciti_safe_no;
    }

    /**
     * @return the zlpay_chnl_code
     */
    public String getZlpay_chnl_code() {
        return zlpay_chnl_code;
    }

    /**
     * @param zlpay_chnl_code the zlpay_chnl_code to set
     */
    public void setZlpay_chnl_code(String zlpay_chnl_code) {
        this.zlpay_chnl_code = zlpay_chnl_code;
    }

    /**
     * @return the zlpay_chnl_merch_name
     */
    public String getZlpay_chnl_merch_name() {
        return zlpay_chnl_merch_name;
    }

    /**
     * @param zlpay_chnl_merch_name the zlpay_chnl_merch_name to set
     */
    public void setZlpay_chnl_merch_name(String zlpay_chnl_merch_name) {
        this.zlpay_chnl_merch_name = zlpay_chnl_merch_name;
    }

    /**
     * @return the reapay_quickpay_version
     */
    public String getReapay_quickpay_version() {
        return reapay_quickpay_version;
    }

    /**
     * @param reapay_quickpay_version the reapay_quickpay_version to set
     */
    public void setReapay_quickpay_version(String reapay_quickpay_version) {
        this.reapay_quickpay_version = reapay_quickpay_version;
    }

    /**
     * @return the reapay_quickpay_debit_url
     */
    public String getReapay_quickpay_debit_url() {
        return reapay_quickpay_debit_url;
    }

    /**
     * @param reapay_quickpay_debit_url the reapay_quickpay_debit_url to set
     */
    public void setReapay_quickpay_debit_url(String reapay_quickpay_debit_url) {
        this.reapay_quickpay_debit_url = reapay_quickpay_debit_url;
    }

    /**
     * @return the reapay_quickpay_merchant_id
     */
    public String getReapay_quickpay_merchant_id() {
        return reapay_quickpay_merchant_id;
    }

    /**
     * @param reapay_quickpay_merchant_id the reapay_quickpay_merchant_id to set
     */
    public void setReapay_quickpay_merchant_id(String reapay_quickpay_merchant_id) {
        this.reapay_quickpay_merchant_id = reapay_quickpay_merchant_id;
    }

    /**
     * @return the reapay_quickpay_seller_email
     */
    public String getReapay_quickpay_seller_email() {
        return reapay_quickpay_seller_email;
    }

    /**
     * @param reapay_quickpay_seller_email the reapay_quickpay_seller_email to set
     */
    public void setReapay_quickpay_seller_email(String reapay_quickpay_seller_email) {
        this.reapay_quickpay_seller_email = reapay_quickpay_seller_email;
    }

    /**
     * @return the reapay_quickpay_notify_url
     */
    public String getReapay_quickpay_notify_url() {
        return reapay_quickpay_notify_url;
    }

    /**
     * @param reapay_quickpay_notify_url the reapay_quickpay_notify_url to set
     */
    public void setReapay_quickpay_notify_url(String reapay_quickpay_notify_url) {
        this.reapay_quickpay_notify_url = reapay_quickpay_notify_url;
    }

    /**
     * @return the reapay_quickpay_url
     */
    public String getReapay_quickpay_url() {
        return reapay_quickpay_url;
    }

    /**
     * @param reapay_quickpay_url the reapay_quickpay_url to set
     */
    public void setReapay_quickpay_url(String reapay_quickpay_url) {
        this.reapay_quickpay_url = reapay_quickpay_url;
    }

    /**
     * @return the reapay_quickpay_key
     */
    public String getReapay_quickpay_key() {
        return reapay_quickpay_key;
    }

    /**
     * @param reapay_quickpay_key the reapay_quickpay_key to set
     */
    public void setReapay_quickpay_key(String reapay_quickpay_key) {
        this.reapay_quickpay_key = reapay_quickpay_key;
    }

    /**
     * @return the reapay_quickpay_pubkey
     */
    public String getReapay_quickpay_pubkey() {
        return reapay_quickpay_pubkey;
    }

    /**
     * @param reapay_quickpay_pubkey the reapay_quickpay_pubkey to set
     */
    public void setReapay_quickpay_pubkey(String reapay_quickpay_pubkey) {
        this.reapay_quickpay_pubkey = reapay_quickpay_pubkey;
    }

    /**
     * @return the reapay_quickpay_prikey
     */
    public String getReapay_quickpay_prikey() {
        return reapay_quickpay_prikey;
    }

    /**
     * @param reapay_quickpay_prikey the reapay_quickpay_prikey to set
     */
    public void setReapay_quickpay_prikey(String reapay_quickpay_prikey) {
        this.reapay_quickpay_prikey = reapay_quickpay_prikey;
    }

    /**
     * @return the reapay_quickpay_sms_url
     */
    public String getReapay_quickpay_sms_url() {
        return reapay_quickpay_sms_url;
    }

    /**
     * @param reapay_quickpay_sms_url the reapay_quickpay_sms_url to set
     */
    public void setReapay_quickpay_sms_url(String reapay_quickpay_sms_url) {
        this.reapay_quickpay_sms_url = reapay_quickpay_sms_url;
    }

    /**
     * @return the reapay_quickpay_credit_url
     */
    public String getReapay_quickpay_credit_url() {
        return reapay_quickpay_credit_url;
    }

    /**
     * @param reapay_quickpay_credit_url the reapay_quickpay_credit_url to set
     */
    public void setReapay_quickpay_credit_url(String reapay_quickpay_credit_url) {
        this.reapay_quickpay_credit_url = reapay_quickpay_credit_url;
    }

    /**
     * @return the reapay_quickpay_pay_url
     */
    public String getReapay_quickpay_pay_url() {
        return reapay_quickpay_pay_url;
    }

    /**
     * @param reapay_quickpay_pay_url the reapay_quickpay_pay_url to set
     */
    public void setReapay_quickpay_pay_url(String reapay_quickpay_pay_url) {
        this.reapay_quickpay_pay_url = reapay_quickpay_pay_url;
    }

    /**
     * @return the reapay_quickpay_prikey_pwd
     */
    public String getReapay_quickpay_prikey_pwd() {
        return reapay_quickpay_prikey_pwd;
    }

    /**
     * @param reapay_quickpay_prikey_pwd the reapay_quickpay_prikey_pwd to set
     */
    public void setReapay_quickpay_prikey_pwd(String reapay_quickpay_prikey_pwd) {
        this.reapay_quickpay_prikey_pwd = reapay_quickpay_prikey_pwd;
    }

    /**
     * @return the reapay_quickpay_query_url
     */
    public String getReapay_quickpay_query_url() {
        return reapay_quickpay_query_url;
    }

    /**
     * @param reapay_quickpay_query_url the reapay_quickpay_query_url to set
     */
    public void setReapay_quickpay_query_url(String reapay_quickpay_query_url) {
        this.reapay_quickpay_query_url = reapay_quickpay_query_url;
    }

    /**
     * @return the reapay_chnl_code
     */
    public String getReapay_chnl_code() {
        return reapay_chnl_code;
    }

    /**
     * @param reapay_chnl_code the reapay_chnl_code to set
     */
    public void setReapay_chnl_code(String reapay_chnl_code) {
        this.reapay_chnl_code = reapay_chnl_code;
    }

    /**
     * @return the reapay_quickpay_bind_url
     */
    public String getReapay_quickpay_bind_url() {
        return reapay_quickpay_bind_url;
    }

    /**
     * @param reapay_quickpay_bind_url the reapay_quickpay_bind_url to set
     */
    public void setReapay_quickpay_bind_url(String reapay_quickpay_bind_url) {
        this.reapay_quickpay_bind_url = reapay_quickpay_bind_url;
    }

    /**
     * @return the order_number_file_path
     */
    public String getOrder_number_file_path() {
        return order_number_file_path;
    }

    /**
     * @param order_number_file_path the order_number_file_path to set
     */
    public void setOrder_number_file_path(String order_number_file_path) {
        this.order_number_file_path = order_number_file_path;
    }

    public String getMember_pay_password_key() {
        return member_pay_password_key;
    }

    public void setMember_pay_password_key(String member_pay_password_key) {
        this.member_pay_password_key = member_pay_password_key;
    }
	
	
}
