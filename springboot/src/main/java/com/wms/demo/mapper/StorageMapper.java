package com.wms.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.demo.entity.Storage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 库存物品表 Mapper 接口
 * </p>
 *
 * @author pengbo
 * @since 2026-04-01
 */
@Mapper
public interface StorageMapper extends BaseMapper<Storage> {

}
