package com.naivebayes;

import java.io.File;
import java.util.ArrayList;

public class ProbabilityCalculator extends Training
{
	
	
	private Patient userinput;
	private File DataSet;
	
	/*
	 * Probability Variables
	 * 
	 * 	NOTE: Variable name with yes represents the probability of patient having symptom true OR false AND having tonsillitis according to the data set, 
	 * 	it does not imply the symptom is true or false.
	 * 	
	 * 	Variables indicate proability of (according to portion of dataset trained):
	 * 
	 * 	p_yes: 			tonsillitis
	 * 	p_no: 			no tonsillitis
	 * 
	 * 	phot: 			patient being hot
	 * 	pnormal:		patient being normal
	 * 	pcool: 			patient being cool
	 * 
	 * 	ptemp_yes: 		patient having given(user input) temperature WITH tonsillitis
	 * 	ptemp_no:		patient having given(user input) temperature WITH NO tonsillitis
	 * 
	 * 	phas_ache: 		patient having aches
	 * 	phas_no_ache:	patient having no aches
	 * 
	 * 	pache_yes:		patient having given(user input) ache WITH tonsillitis
	 * 	pache_no:		patient having given(user input) ache WITH NO tonsillitis
	 * 
	 * 	psore:			patient being sore
	 * 	pnot_sore:		patient not being sore
	 * 
	 * 	psore_yes:		patient having given(user input) soreness WITH tonsillitis
	 * 	psore_no:		patient having given(user input) soreness WITH NO tonsillitis
	 * 
	 *	pinstance_yes:	patient having all three given(user input) symptoms WITH tonsillitis
	 *	pinstance_no:	patient having all three given(user input) symptoms WITHOUT tonsillitis
	 * 
	 * 	Example: If patient being evaluated has no aches, pache_yes represents the probability of the patient having no aches AND having tonsillitis 
	 * 	according to the data set.
	 */
	
	private double p_yes;
	private double p_no;
	
	private double ptemp_yes;
	private double ptemp_no;
	
	private double pache_yes;
	private double pache_no;
	
	private double psore_yes;
	private double psore_no;
	
	private double pinstance;
	private double pinstance_yes;
	private double pinstance_no;
	private double pinst_temp;
	private double pinst_ache;
	private double pinst_sore;
	private double ptonsillitis_given_instance;
	private double pno_tonsillitis_given_instance;
	
	
	public ProbabilityCalculator() 
	{
		
	}
	
	public void NaiveBayesAlgorithm(Patient patient, double tonsillitis_count, double hot_count, double norm_count, double cool_count, double ache_count, 
														double no_ache_count, double sore_count, double not_sore_count, double num_of_patients, ArrayList<Boolean> hotarray, 
															ArrayList<Boolean> normalarray, ArrayList<Boolean> coolarray, ArrayList<Boolean> achearray, ArrayList<Boolean> no_achearray, 
																ArrayList<Boolean> sorearray, ArrayList<Boolean> not_sorearray)
	{
		p_yes = tonsillitis_count / num_of_patients;
		p_no = (num_of_patients - tonsillitis_count) / num_of_patients;
		
		switch(patient.getTemperature())
		{
			case "hot":
			{
				System.out.println(" \n\n  " + hot_count + ", " + num_of_patients + " ," + tonsillitis_count);
				pinst_temp = hot_count / num_of_patients;
				ptemp_yes = hot_with_tonsillitis(hotarray) / tonsillitis_count;
				ptemp_no = hot_without_tonsillitis(hotarray) / (num_of_patients - tonsillitis_count);
				System.out.println(" \n\n  " + pinst_temp + ", " + ptemp_yes + ", " + ptemp_no);
			}
			case "normal":
			{
				pinst_temp = norm_count / num_of_patients;
				ptemp_yes = normal_with_tonsillitis(normalarray) / tonsillitis_count;
				ptemp_no = normal_without_tonsillitis(normalarray) / num_of_patients - tonsillitis_count;
			}
			case "cool":
			{
				pinst_temp = cool_count / num_of_patients;
				ptemp_yes = cool_with_tonsillitis(coolarray) / tonsillitis_count;
				ptemp_no = cool_without_tonsillitis(coolarray) / num_of_patients - tonsillitis_count;
			}
			default:
			{
				System.out.println("ERROR: Switch in NaiveBayes method not functional. ");
			}
		}
		
		if(patient.isAches())
		{
			pinst_ache = ache_count / num_of_patients;
			pache_yes = ache_with_tonsillitis(achearray) / tonsillitis_count;
			pache_no = ache_without_tonsillitis(achearray) / num_of_patients - tonsillitis_count;
		}
		else
		{
			pinst_ache = no_ache_count / num_of_patients;
			pache_yes = no_ache_with_tonsillitis(no_achearray) / tonsillitis_count;
			pache_no = no_ache_without_tonsillitis(no_achearray) / num_of_patients - tonsillitis_count;
		}
		
		if(patient.isSoreThroat())
		{
			pinst_sore = sore_count / num_of_patients;
			psore_yes = sore_with_tonsillitis(sorearray) / tonsillitis_count;
			psore_yes = sore_without_tonsillitis(sorearray) / num_of_patients - tonsillitis_count;
		}
		else
		{
			pinst_sore = not_sore_count / num_of_patients;
			psore_yes = not_sore_with_tonsillitis(not_sorearray) / tonsillitis_count;
			psore_no = not_sore_without_tonsillitis(not_sorearray) / num_of_patients - tonsillitis_count;
		}
		
		
		/*
		 * X = instance
		 * 
		 * 
		 * [P(X | tonsillitis = yes)] * [(P(tonsillitis)]
		 * 
		 */
		pinstance_yes = ptemp_yes * pache_yes * psore_yes * p_yes;
		
		//P(X | tonsillitis = no) * (P(tonsillitis)
		pinstance_no = ptemp_no * pache_no * psore_no * p_no;
		
		//P(X)
		pinstance = pinst_temp * pinst_ache * pinst_sore;
		
		//P(Tonsillitis | X) = [P(X | Tonsillitis = yes)] * [P(tonsillitis = yes)] / P(X)
		ptonsillitis_given_instance = pinstance_yes / pinstance;
		
		//P(No Tonsillitis | X) = [P(X | Tonsillitis = no)] * [P(tonsillitis = yes)] / P(X)
		pno_tonsillitis_given_instance = pinstance_no / pinstance;
		
	}
	
	
	/*
	 * 	The methods below use for loops to step through the training data and count how many times a given symptom occurs WITH tonsillitis
	 * 	and how many times a symptom occurs WITHOUT tonsillitis. This data is used in the Naive Bayes algorithm in the instanceEvaluator method.
	 */
	
	public int hot_with_tonsillitis(ArrayList<Boolean> hot)
	{
		int counter = 0;
		
		for(int i=0 ; i<hot.size() ; i++)
		{
			if(hot.get(i))
			{
				counter++;
			}
			else
			{
				
			}
			System.out.println(" \n\n counter is   " + counter);
		}
		
		return counter;
	}
	
	public int hot_without_tonsillitis(ArrayList<Boolean> hot)
	{
		int counter = 0;
		
		for(int i=0 ; i<hot.size() ; i++)
		{
			if(hot.get(i))
			{
				
			}
			else
			{
				counter++;
			}
		}
		
		return counter;
	}
	
	public int cool_with_tonsillitis(ArrayList<Boolean> cool)
	{
		int counter = 0;
		
		for(int i=0 ; i<cool.size() ; i++)
		{
			if(cool.get(i))
			{
				counter++;
			}
			else
			{
				
			}
			
		}
		
		return counter;
	}
	
	public int cool_without_tonsillitis(ArrayList<Boolean> cool)
	{
		int counter = 0;
		
		for(int i=0 ; i<cool.size() ; i++)
		{
			if(cool.get(i))
			{
				
			}
			else
			{
				counter++;
			}
			
		}
		
		return counter;
	}
	
	public int normal_with_tonsillitis(ArrayList<Boolean> normal)
	{
		int counter = 0;
		
		for(int i=0 ; i<normal.size() ; i++)
		{
			if(normal.get(i))
			{
				counter++;
			}
			else
			{
				
			}
			
		}
		
		return counter;
	}
	
	public int normal_without_tonsillitis(ArrayList<Boolean> normal)
	{
		int counter = 0;
		
		for(int i=0 ; i<normal.size() ; i++)
		{
			if(normal.get(i))
			{
				
			}
			else
			{
				counter++;
			}
			
		}
		
		return counter;
	}
	
	public int ache_with_tonsillitis(ArrayList<Boolean> ache)
	{
		int counter = 0;
		
		for(int i=0 ; i<ache.size() ; i++)
		{
			if(ache.get(i))
			{
				counter++;
			}
			else
			{
				
			}
			
		}
		
		return counter;
	}
	
	public int ache_without_tonsillitis(ArrayList<Boolean> ache)
	{
		int counter = 0;
		
		for(int i=0 ; i<ache.size() ; i++)
		{
			if(ache.get(i))
			{
				
			}
			else
			{
				counter++;
			}
			
		}
		
		return counter;
	}
	
	public int no_ache_with_tonsillitis(ArrayList<Boolean> no_ache)
	{
		int counter = 0;
		
		for(int i=0 ; i<no_ache.size() ; i++)
		{
			if(no_ache.get(i))
			{
				counter++;
			}
			else
			{
				
			}
			
		}
		
		return counter;
	}
	
	public int no_ache_without_tonsillitis(ArrayList<Boolean> no_ache)
	{
		int counter = 0;
		
		for(int i=0 ; i<no_ache.size() ; i++)
		{
			if(no_ache.get(i))
			{
				
			}
			else
			{
				counter++;
			}
			
		}
		
		return counter;
	}
	
	public int sore_with_tonsillitis(ArrayList<Boolean> sore)
	{
		int counter = 0;
		
		for(int i=0 ; i<sore.size() ; i++)
		{
			if(sore.get(i))
			{
				counter++;
			}
			else
			{
				
			}
			
		}
		
		return counter;
	}
	
	public int sore_without_tonsillitis(ArrayList<Boolean> sore)
	{
		int counter = 0;
		
		for(int i=0 ; i<sore.size() ; i++)
		{
			if(sore.get(i))
			{
				
			}
			else
			{
				counter++;
			}
			
		}
		
		return counter;
	}
	
	public int not_sore_with_tonsillitis(ArrayList<Boolean> not_sore)
	{
		int counter = 0;
		
		for(int i=0 ; i<not_sore.size() ; i++)
		{
			if(not_sore.get(i))
			{
				counter++;
			}
			else
			{
				
			}
			
		}
		
		return counter;
	}
	
	public int not_sore_without_tonsillitis(ArrayList<Boolean> not_sore)
	{
		int counter = 0;
		
		for(int i=0 ; i<not_sore.size() ; i++)
		{
			if(not_sore.get(i))
			{
				
			}
			else
			{
				counter++;
			}
			
		}
		
		return counter;
	}
	
	public double getP_yes() {
		return p_yes;
	}

	public void setP_yes(double p_yes) {
		this.p_yes = p_yes;
	}

	public double getP_no() {
		return p_no;
	}

	public void setP_no(double p_no) {
		this.p_no = p_no;
	}

	public double getPtemp_yes() {
		return ptemp_yes;
	}

	public void setPtemp_yes(double ptemp_yes) {
		this.ptemp_yes = ptemp_yes;
	}

	public double getPtemp_no() {
		return ptemp_no;
	}

	public void setPtemp_no(double ptemp_no) {
		this.ptemp_no = ptemp_no;
	}

	public double getPache_yes() {
		return pache_yes;
	}

	public void setPache_yes(double pache_yes) {
		this.pache_yes = pache_yes;
	}

	public double getPache_no() {
		return pache_no;
	}

	public void setPache_no(double pache_no) {
		this.pache_no = pache_no;
	}

	public double getPsore_yes() {
		return psore_yes;
	}

	public void setPsore_yes(double psore_yes) {
		this.psore_yes = psore_yes;
	}

	public double getPsore_no() {
		return psore_no;
	}

	public void setPsore_no(double psore_no) {
		this.psore_no = psore_no;
	}

	public double getPinstance() {
		return pinstance;
	}

	public void setPinstance(double pinstance) {
		this.pinstance = pinstance;
	}

	public double getPinstance_yes() {
		return pinstance_yes;
	}

	public void setPinstance_yes(double pinstance_yes) {
		this.pinstance_yes = pinstance_yes;
	}

	public double getPinstance_no() {
		return pinstance_no;
	}

	public void setPinstance_no(double pinstance_no) {
		this.pinstance_no = pinstance_no;
	}

	public double getPinst_temp() {
		return pinst_temp;
	}

	public void setPinst_temp(double pinst_temp) {
		this.pinst_temp = pinst_temp;
	}

	public double getPinst_ache() {
		return pinst_ache;
	}

	public void setPinst_ache(double pinst_ache) {
		this.pinst_ache = pinst_ache;
	}

	public double getPinst_sore() {
		return pinst_sore;
	}

	public void setPinst_sore(double pinst_sore) {
		this.pinst_sore = pinst_sore;
	}

	public double getPtonsillitis_given_instance() {
		return ptonsillitis_given_instance;
	}

	public void setPtonsillitis_given_instance(double ptonsillitis_given_instance) {
		this.ptonsillitis_given_instance = ptonsillitis_given_instance;
	}

	public double getPno_tonsillitis_given_instance() {
		return pno_tonsillitis_given_instance;
	}

	public void setPno_tonsillitis_given_instance(double pno_tonsillitis_given_instance) {
		this.pno_tonsillitis_given_instance = pno_tonsillitis_given_instance;
	}
}
