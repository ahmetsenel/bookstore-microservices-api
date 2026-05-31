package com.ahmetsenel.common.event;

import com.ahmetsenel.common.dto.ItemInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class NotificationEvent {

    private Long OrderId;

    private String email;

    private String address;

    private BigDecimal totalPrice;

    private List<ItemInfo> items;
}
