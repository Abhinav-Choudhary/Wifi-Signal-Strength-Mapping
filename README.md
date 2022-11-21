# Problem Description
With the growing popularity of smart devices and the introduction of newer wireless transmission standards which use higher frequency waves but have a shorter range, it is evermore imperative to locate the most efficient spot in the house to ensure every corner gets the best possible reception. 
The Wi-Fi signal will always go weak in some corners of your house, which is very annoying. Sometimes you wish to watch the news in your toilet but cannot access the Wi-Fi. Usually, it is because the Wi-Fi signal is an electromagnetic wave, and it gets blocked and weakened by concrete walls and wooden doors in your house. That situation can be adjusted by changing your Wi-Fi router to a better place where it can cover more places in your house. Thus, we aim to create a program that can provide a heatmap of the varying intensity of the Wi-Fi signal at different places, so that the users can determine the most ideal spot to install the Wi-Fi router.

# Idea for solving the problem
We plan to simulate the Wi-Fi signal strength in the house using the computational physics method. 
The behaviour of a Wi-Fi signal, also known as an electromagnetic wave, is defined by the 
Helmholtz equation:
![alt text](C:\Users\souvi\Downloads\equation.png)

So, if we can solve the Helmholtz equation with boundary condition (the structure of the house), 
the result will be the Wi-Fi signal strength in the whole house.
The software should be able to use a 2-dimension floor plan as input, along with the point where 
the Wi-Fi router is placed and the frequency of the signal. An example of floor plan input is as 
below:
![alt text](C:\Users\souvi\Downloads\floor-plan.png)
