################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/EnergyComputationAlgo.cpp \
../src/EnergyComputationAlgoBridge.cpp \
../src/Tester.cpp 

CPP_DEPS += \
./src/EnergyComputationAlgo.d \
./src/EnergyComputationAlgoBridge.d \
./src/Tester.d 

OBJS += \
./src/EnergyComputationAlgo.o \
./src/EnergyComputationAlgoBridge.o \
./src/Tester.o 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp src/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -DNDEBUG -I/Library/Java/JavaVirtualMachines/jdk-18.0.2.1.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk-18.0.2.1.jdk/Contents/Home/include/darwin -O3 -g3 -Wall -c -fmessage-length=0 -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src

clean-src:
	-$(RM) ./src/EnergyComputationAlgo.d ./src/EnergyComputationAlgo.o ./src/EnergyComputationAlgoBridge.d ./src/EnergyComputationAlgoBridge.o ./src/Tester.d ./src/Tester.o

.PHONY: clean-src

