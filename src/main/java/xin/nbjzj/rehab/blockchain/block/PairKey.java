/**
 * Project Name:trustsql_sdk
 * File Name:PairKey.java
 * Package Name:com.tencent.trustsql.sdk.bean
 * Date:Jul 26, 201710:27:04 AM
 * Copyright (c) 2017, Tencent All Rights Reserved.
 */

package xin.nbjzj.rehab.blockchain.block;

import lombok.Data;

/**
 * ClassName:PairKey <br/>
 * Date:     Jul 26, 2017 10:27:04 AM <br/>
 * @author Rony
 * @since JDK 1.7
 */
@Data
public class PairKey {

    private String publicKey;
    private String privateKey;
    
	public PairKey(String publicKey, String privateKey) {
		super();
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	public PairKey() {
		super();
	}


}

