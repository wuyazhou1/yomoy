package com.nsc.Amoski.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Data;

import javax.persistence.*;

/**
 * 订单支付金
 * @author huafeng
 */
@Entity
@Table(name = "TB_ORDER_PAYMENT")
@Data
public class TbOrderPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
	@SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_ORDER_PAYMENT_SEQUENCE")
	private Integer id;//ID
	@Column(name = "serial_number")
	private String serialNumber;//订单编号
	@Column(name = "member_id")
	private Integer memberId;//用户ID
	@Column(name = "amount")
	private BigDecimal amount;//金额
	@Column(name = "status")
	private Integer status;//状态(1=成功、2=处理中)
	@Column(name = "open_date")
	private Timestamp openDate;//申请日期
	@Column(name = "remark")
	private String remark;//备注
	@Column(name = "bank_flow")
	private String bankFlow;//银行流水
	/*@Column(name = "NAME")
	private String pay_ip;//ip */
	@Column(name = "return_status")
	private String returnStatus;//银行返回状态
	@Column(name = "service_charge")
	private BigDecimal serviceCharge;//手续费
	@Column(name = "pay_type")
	private String payType;//支付方式(1.微信 2.支付宝)
	@Column(name = "payment_account")
	private Integer paymentAccount;//支付账号

}
