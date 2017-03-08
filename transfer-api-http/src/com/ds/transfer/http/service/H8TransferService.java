package com.ds.transfer.http.service;

import com.ds.transfer.common.service.TransferService;

/**
 * H8转账业务
 * 
 * @author jackson
 *
 */
public interface H8TransferService<T> extends TransferService<T>, SupportTransferService<T>, SupportH8Service {

}
