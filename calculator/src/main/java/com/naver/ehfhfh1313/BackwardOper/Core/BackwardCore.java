package com.naver.ehfhfh1313.BackwardOper.Core;

import java.util.Scanner;
import java.math.*;

import com.naver.ehfhfh1313.BackwardOper.Operator.ABS_CalMember;
import com.naver.ehfhfh1313.BackwardOper.Operator.ABS_Calculer;
import com.naver.ehfhfh1313.BackwardOper.Operator.NumOperand;
import com.naver.ehfhfh1313.BackwardOper.Stack.Stack;

public class BackwardCore {

	   public static void main(String[] args) throws ClassNotFoundException
	   {
		   Scanner scanner = new Scanner(System.in);
		   String input;
		   boolean check = true;
	
		   while(check)
		   {
			   System.out.println("식을 입력하세요.");
			   Stack<ABS_CalMember> value = new Stack<ABS_CalMember>(3);
			   input = scanner.nextLine();
			 
			   try { postfixExp(input, value); }// 후위 표기 스택 가져오기.
			   catch(Exception e)
			   {
				   e.printStackTrace();
				   System.out.println("\n다시 입력 하세요.");
				   continue;
			   }
			   
			   try { System.out.println("\n연산 결과입니다: " + getValue(value)); }
			   catch(Exception e)
			   { System.out.println("\n다시 입력 하세요."); }
			   	    
			   System.out.print("계속하시겠습니까?(Y/N)");
			   input = scanner.nextLine();
			   if(input.equals("y") || input.equals("Y")) check=true;
			   else check = false;
			   
		   }
		   System.out.println("프로그램을 종료합니다.");
		   scanner.close();
	   }
	   
	   private static Stack<ABS_CalMember> postfixExp(String str, Stack<ABS_CalMember> calStack)
	   {// 5월 2일 연산 과정 표시 수정.
		   Stack<ABS_CalMember> tempStack = new Stack<ABS_CalMember>(calStack.getSlotSize());// 연산을 위한 임시스택
		   
		   BigInteger taskinteger = new BigInteger("0");// 추출중인 숫자.-------------------------------------------------------------
		   
		   
		   boolean numberTask = false;// 숫자 추출 작업 상태를 표시.
		   
		   for(int i = 0; i < str.length(); i++)
		   {// 한 문자씩 읽기.
			   char taskChar = str.charAt(i);
			   if(taskChar >= '0' && taskChar <= '9')
			   {
				   if(!numberTask) taskinteger = BigInteger.ZERO;          // 0대입		   
				   taskinteger = (taskinteger.multiply(BigInteger.valueOf(10))).add(BigInteger.valueOf((taskChar - '0')));  //BigInteger로 바꿔서 계산
				   numberTask = true;
			   }
			   else if(ABS_CalMember.isExist(String.valueOf(taskChar)))
			   {// 입력한 문자가 연산 토큰일 경우.
				   ABS_CalMember operator = ABS_CalMember.getInstance(String.valueOf(taskChar));
				   if(numberTask)
				   {
					   calStack.pushBack(new NumOperand(taskinteger));////////////////////
					   numberTask = false;
				   }
				   if(operator.toString().equals("("))
				   {// (를 만나면 임시 스택에 푸시한다.
					   tempStack.pushBack(operator);
				   }
				   else if(operator.toString().equals(")"))
				   {// )를 만나면 임시 스택에서 ( 가 나올 때까지 팝하고, (는 임시 스택에서 팝하여 
	   
					   while(tempStack.size() >= 0)
					   {
						   if(tempStack.getBack().toString().equals("("))
						   {
							   tempStack.popBack();
							   break;
						   }
						   calStack.pushBack(tempStack.getBack());
						   tempStack.popBack();
					   }
				   }
				   else if(operator instanceof ABS_Calculer)
				   {// 괄호가 아닐경우.
					   ABS_Calculer calOper = (ABS_Calculer)operator;
					   while(true)
					   {// 연산자를 만나면 임시 스택에서 그 연산자보다 낮은 우선순위의 

						   // 후위 표기 스택에 저장한 뒤에 자신을 푸시한다.
						   if(tempStack.size() == 0)
						   {// 임시 스택이 비었을 경우 빠져나감.
							   tempStack.pushBack(operator);
							   break;
						   }
						   ABS_Calculer calculer = tempStack.getBack() instanceof ABS_Calculer ? (ABS_Calculer)tempStack.getBack() : null;
						   if(calculer == null || calculer.getPriority() < calOper.getPriority())
						   {// 낮은 우선순위의 연산자를 만났거나, 괄호를 만났을경우 빠져나감.
							   tempStack.pushBack(calOper);
							   break;
						   }
						   calStack.pushBack(tempStack.getBack());
						   tempStack.popBack();
					   }
				   }
			   }
		   }
		   if(numberTask) calStack.pushBack(new NumOperand(taskinteger));
		   while(tempStack.size() > 0)
		   {// 마지막 남은 연산자들을 푸시.
			   calStack.pushBack(tempStack.getBack());
			   tempStack.popBack();
		   }
		   return calStack;
	   }
	   private static NumOperand getValue(Stack<ABS_CalMember> postfixStack)
	   {// 후위 표기 스택을 바탕으로 값 가져오기.
		   Stack<NumOperand> tempNumStack = new Stack<NumOperand>(postfixStack.getSlotSize());
		   for(int i = 0; i < postfixStack.size(); i++)
		   {
			   ABS_CalMember taskMember = postfixStack.getMemberAt(i);
			   NumOperand x, y, number;
			   ABS_Calculer calculer;
			   if(taskMember instanceof NumOperand)
			   {// 읽은 멤버가 피연산자라면.
				   number = (NumOperand)taskMember;
				   tempNumStack.pushBack(number);
			   }
			   else if(taskMember instanceof ABS_Calculer)
			   {// 읽은 멤버가 연산자라면.
				   calculer = (ABS_Calculer)taskMember;
				   // 임시 스택에서 두 피 연산자를 꺼낸다음 연산한 다음 결과를 다시 푸시.
				   x = tempNumStack.getBack(); tempNumStack.popBack();
				   y = tempNumStack.getBack(); tempNumStack.popBack();
				   tempNumStack.pushBack(calculer.task(y, x));
			   }
		   }
		   if(tempNumStack.getMemberAt(0) == null) throw new NullPointerException();
		   return tempNumStack.getMemberAt(0);// 임시 스택에 마지막 남은 숫자가 결과.
	   }
}
