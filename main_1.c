/*
    * File Name: main_1.c
    * Author: Kieran Kilgannon
    * Trip time in milliseconds recorded and sent to the Raspberry PI 
*/

#include <pic16f1824.h>

#include "mcc_generated_files/mcc.h"
#include "stdio.h"
#include "Usart.h"
#include "Timers.h"
#include "stdlib.h"
//#include "interrupt_manager.h"

unsigned volatile int tick_count;
unsigned volatile int yourtime1=0;
unsigned volatile int yourtime2=0;
int buttonpress=0;
int flag1=0;
int flag2=0;
unsigned int interval = 0;
int x=0;

void interrupt InterruptServiceRoutine(void);

void main(void)
{
    // initialize the device
    SYSTEM_Initialize();
    InitUSART();
    InitialiseTimers();

    INTCONbits.GIE = 1;  //Global Interrupt Enable
    INTCONbits.PEIE = 1;  //Peripheral Interrupt Enable

    printf("READY\r\n");
   
    while(1)
    {
        if(flag1==1)
        { 
            while(flag2==0){} //wait for second interrupt !flag2
            //printf("time  1: %i ",yourtime1);
            //printf("time  2: %i ",yourtime2);
            __delay_ms(100);
            interval = yourtime2 - yourtime1;
            printf("Triptime: %i Reg is: %i \r\n",(yourtime2-yourtime1),(x));
            //printf(" Reg is: %i \r\n", x);
            __delay_ms(1000);
            flag1 = 0;
            flag2 = 0;
        }   
    }  
}//End main


void interrupt InterruptServiceRoutine(void)
{
    if(TMR0IF)
    {
        tick_count++;
        //TMR0=117;
        TMR0IF=0;
    }
    
    if(IOCAFbits.IOCAF4 == 1 )
    {
        tick_count=0;
        //x = TMR0;
        TMR0 = 0;
        flag1 = 1;
        //printf(" %i hello" ,x);
        IOCAFbits.IOCAF4 = 0;
        INTCONbits.IOCIF = 0;
        //printf("1");
//        INTCONbits.IOCIE = 0;
    }
    
    if(INTCONbits.INTE == 1 && INTCONbits.INTF == 1)
    {
        yourtime2 = tick_count;
        x = TMR0;
        INTCONbits.INTF = 0;
        flag2 = 1;
    }
}//End ISR
