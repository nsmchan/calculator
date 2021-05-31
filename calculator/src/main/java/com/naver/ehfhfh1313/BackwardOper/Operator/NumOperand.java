package com.naver.ehfhfh1313.BackwardOper.Operator;

import java.math.*;

public class NumOperand extends ABS_CalMember
{// 숫자 피연산자.
	
	private BigInteger value ;
	public NumOperand(BigInteger v)
	{
		this.value = v;
	}

	public BigInteger getValue()
	{
		return this.value;
	}
	@Override public String toString() { return String.valueOf(this.value); }
}