package com.tailorpro.dto;
import java.math.BigDecimal;
public record DashboardResponse(long totalCustomers, long totalOrders, long pendingOrders, long readyOrders, long deliveredOrders, long todayDelivery, long monthlyOrders, BigDecimal monthlyRevenue) {}
