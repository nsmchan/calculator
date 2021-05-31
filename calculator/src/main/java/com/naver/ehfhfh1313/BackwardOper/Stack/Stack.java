package com.naver.ehfhfh1313.BackwardOper.Stack;

/*
* 스택 클래스.
* 연결 리스트를 베이스로 구성하여 동적인 확장 가능.
*/
/*
* 5월 2일 버그 수정
* 인덱스 로 스택 멤버에 접근할 때 잘못 접근되던 문제를 수정
*/
public final class Stack<T>
{
	private final int slotSize;
	private final int fullSize;// -1일경우 제한 없음.
	private StackLinkSlot<T> lastSlot;
	public Stack() { this(10); }
	public Stack(int slotSize) { this(slotSize, -1); }
	public Stack(int slotSize, int fullSize)
	{// 슬롯 사이즈와 스택 풀 사이즈를 결정.
		this.slotSize = slotSize;
		this.fullSize = fullSize;
		this.lastSlot = new StackLinkSlot<T>(slotSize, null);
	}
	public void pushBack(T member)
	{// 푸시.
		if(this.fullSize != -1 && this.size() > this.fullSize)
		{// 스택 풀일경우.
			throw new StackOverflowError();
		}
		if(this.lastSlot.isFull())
		{// 슬롯의 공간이 가득 찼을경우.
			this.lastSlot = this.lastSlot.createBackSlot();
		}
		this.lastSlot.push(member);
	}
	public T getBack()
	{// 스택의 가장 위 얻기.
		return this.lastSlot.getBack();
	}
	public void popBack()
	{// 팝.
		if(this.lastSlot.getFrontSlot() == null && this.lastSlot.isEmpty())
		{
			throw new StackOverflowError();
		}
		this.lastSlot.popBack();
		if(this.lastSlot.getFrontSlot() != null && this.lastSlot.isEmpty())
		{// 마지막 슬롯이 비었을경우 삭제함.
			this.lastSlot = this.lastSlot.getFrontSlot();
			this.lastSlot.deleteBackSlot();
		}
}
	
	public T getMemberAt(int index)
	{// 인덱스로 꺼내기. (5월 2일 최종 수정)
		StackLinkSlot<T> taskSlot = this.lastSlot;
		if (this.size() - index <= 0 || index < 0)
		{// 인덱스 초과 혹은 미만.
			throw new IndexOutOfBoundsException();
		}
		int deeps = (this.size() - 1) / this.slotSize;// 전체 슬롯의 깊이입니다.
		int floorDownCount = (deeps + 1) * this.slotSize - 1 - index;// top 부터 내려갈 카운트 개수입니다.
		int inSlotIndex = (this.slotSize - (floorDownCount % this.slotSize) - 1);// 찾은 슬롯에서의 해당멤버 위치입니다.
		for(int i = floorDownCount / this.slotSize ; i > 0; --i)
		{// i는 찾아 내려갈 슬롯 깊이 입니다.
			taskSlot = taskSlot.getFrontSlot();// 인덱스에 해당하는 데이터를 가지고 있는 슬롯을 만날때까지 내려갑니다.
		}
		return taskSlot.getMember(inSlotIndex);// 멤버 찾아서 리턴.
	}
	
	public int size()
	{// 스택의 크기 리턴.
		int count = -1;
		StackLinkSlot<T> taskSlot = this.lastSlot;
		do
		{
			taskSlot = taskSlot.getFrontSlot();
			++count;
		}
		while (taskSlot != null);
		return count * this.slotSize + this.lastSlot.getSize();
	}
	public int getFullSize()
	{// 스택 풀 사이즈 얻기.
		return this.fullSize;
	}
	public int getSlotSize()
	{// 슬롯 사이즈 얻기.
		return this.slotSize;
	}
	@Override public String toString()
	{
		String returnString = "";
		for(int i = 0; i < this.size(); i++)
		{
			returnString += this.getMemberAt(i).toString() + " ";
		}
		return returnString;
	}
}
/*
* 스택의 슬롯.
* 링크드 리스트 방식.
* 데이터는 배열로 관리.
*/
final class StackLinkSlot<T>
{
	private Object[] members;// 데이터 저장소.
	private final int memberArrSize;
	private int stackHeight = 0;
	private StackLinkSlot<T> frontSlot = null, backSlot = null;
	public StackLinkSlot(int size, StackLinkSlot<T> frontSlot)
	{// 사이즈, 바로 앞 슬롯 받기.
		this.members = new Object[size];
		this.memberArrSize = size;
		this.frontSlot = frontSlot;
	}
	public StackLinkSlot<T> getFrontSlot()
	{// 바로 앞 슬롯 리턴.
		return this.frontSlot;
	}
	public StackLinkSlot<T> createBackSlot()
	{// 뒤 슬롯 생성.
		this.backSlot = new StackLinkSlot<T>(this.memberArrSize, this);
		return this.backSlot;
	}
	public void deleteBackSlot()
	{// 뒤 슬롯 삭제.
		this.backSlot = null;
	}
	public boolean isFull()
	{// 꽉 찼을때 true.
		return stackHeight >= memberArrSize;
	}
	public boolean isEmpty()
	{// 비었을때 true.
		return this.stackHeight <= 0;
	}
	public int getSize()
	{// 슬롯에 넣을 수 있는 데이터 개수.
		return this.stackHeight;
	}
	public void push(T member)
	{// 슬롯에 데이터 푸시.
		if(member == null) System.out.println("ERR NULL");
		this.members[this.stackHeight] = member;
		++this.stackHeight;
	}
	@SuppressWarnings("unchecked") public T getBack()
	{// 슬롯의 가장 뒤 데이터 얻기.
		return (T)this.members[this.stackHeight - 1];
	}
	@SuppressWarnings("unchecked") public T getMember(int index)
	{// 슬롯에서 인덱스로 멤버 얻기.
		return (T)this.members[index];
	}
	public void popBack()
	{// 팝.
		--this.stackHeight;
	}
 
}


