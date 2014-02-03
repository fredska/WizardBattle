package com.wb.entity;


public abstract class Projectile extends Entity {

	//Does the projectile heal whatever it collides with?
	private boolean heal;
	
	public Projectile(boolean collidable) {
		super(collidable);
		heal = false;
	}
	
	public boolean canHeal(){ return this.heal;}
	public void setHeal(boolean canHeal){
		this.heal = canHeal;
	}

}
