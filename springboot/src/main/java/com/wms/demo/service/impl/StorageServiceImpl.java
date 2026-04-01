package com.wms.demo.service.impl;

import com.wms.demo.entity.Storage;
import com.wms.demo.mapper.StorageMapper;
import com.wms.demo.service.IStorageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存物品表 服务实现类
 * </p>
 *
 * @author pengbo
 * @since 2026-04-01
 */
@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements IStorageService {

}
