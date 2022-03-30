/***********/
/* PACKAGE */
/***********/
package CFG;

import java.util.HashMap;
/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import TEMP.*;
import MIPS.*; 
//import basicBlock;
/*******************/
/* PROJECT IMPORTS */
/*******************/
public class colorGraph{
	public class node{
		String name;//who we represent
		HashSet<String> neig;
		int activeNeig;
		String paint;
		public node(String name){
			this.name=name;
			this.neig=new HashSet<String>();
			this.activeNeig=0;
			this.paint=null;
		}

	} 
	HashSet<node> valid=new HashSet<node>();  //nodes currently in graph
	HashSet<node> overall=new HashSet<node>();  //nodes we have to deal with in general
	 //compare something to it's offset to get unique thing
	//change the names to be X_offset

	public colorGraph(basicBlock h) {
		basicBlock curr=h;
		while (curr!=null){
			Iterator<String> it=curr.inSet.iterator();
			node p;
			while(it.hasNext()){
				String n=it.next();
				p=find(n);
				if(p==null){//if it does not exsists
					p=new node(n);
					this.overall.add(p);
					this.valid.add(p);
				}
				Iterator<String> o=curr.inSet.iterator();
				while(o.hasNext()){
					String s=o.next();
					if(!s.equals(p.name)){
						p.neig.add(s);
						
					}
				}
				p.activeNeig=p.neig.size();
				
			}
			curr=curr.direct;
		
			
		}
			

	}
	public node find(String check){
		Iterator<node> go=this.valid.iterator();
		while(go.hasNext()){
			node maybe=go.next();
			if(maybe.name==check){
				return maybe;

			}
		}
		return null;


	}
	public Boolean isEmpty(){
		return this.valid.isEmpty();
	}

}