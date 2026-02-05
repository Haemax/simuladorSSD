#!/bin/bash

javac SimSSD.java

# parametros passados pelo usuario ou padrao
B=${1:-100}   # blocos
P=${2:-4}     #  paginas por bloco
O=${3:-0.1}   # overprovisioning
TR=${4:-50}   # tempo leitura 
TP=${5:-500}  # tempo escrita 
TE=${6:-2000} # tempo erase 

# exemplo de uso: ./run.sh 100 4 0.1 50 500 2000 < trace.txt
java FTLSim $B $P $O $TR $TP $TE
