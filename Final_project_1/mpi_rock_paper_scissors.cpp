#include <iostream>
#include <mpi.h>
#include <cstdlib>
#include <ctime>

using namespace std;

/********************************************************************************
* file: mpi_rock_paper_scissors.cpp
* author: R. Ramirez
* class: CS 3700 – Parallel Processing
*
* assignment: Final project 1
* date last modified: 4/22/2020
*
* purpose: Implement rock paper scissors with three players. Players are  
*          individual processes that communicate without a coordinator process.
*
**********************************************************************************/

void calculateScore(int*, int, int*);

int main (int argc, char *argv[]){
  
  int rank, size, score;
  int allGestures[3];

  MPI_Init(&argc, &argv);
  MPI_Comm_size(MPI_COMM_WORLD, &size);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  
  // each node has a unique srand seed
  srand(time(NULL) + rank);
  
  for ( int i = 0; i < atoi(argv[1]); i++ ){
    // (0 -> rock), (1 -> paper), (2 -> scissors)
    int randNum = rand() % 3;

    // pass individual randNums to each rank/process including self
    MPI_Allgather(&randNum, 1, MPI_INT, &allGestures, 1, MPI_INT, MPI_COMM_WORLD);
    
    // calculate score
    calculateScore(allGestures, rank, &score);
    
    if (rank == 0)
      cout << "Iteration " << i << " choices: (" << allGestures[0] << ", " << allGestures[1] << ", " << allGestures[2] << ")\n";
  }

  cout << "Rank: " << rank << " score: " << score << "\n";

  MPI_Finalize();
  
  return 0;
}

void calculateScore(int* choices, int nodeRank, int* rankScore){
  // rank 0 calls these 
  if (nodeRank == 0){
    if (choices[nodeRank] == 0){
      if (choices[1] == 2 && choices[2] == 2)
        *rankScore += 2;
      else if (choices[1] == 0 && choices[2] == 2)
        *rankScore += 1;
      else if (choices[1] == 2 && choices[2] == 0)
        *rankScore += 1;
    }else if (choices[nodeRank] == 1){
      if (choices[1] == 0 && choices[2] == 0)
        *rankScore += 2;
      else if (choices[1] == 1 && choices[2] == 0)
        *rankScore += 1;
      else if (choices[1] == 0 && choices[2] == 1)
        *rankScore += 1;
    }else{
      if (choices[1] == 1 && choices[2] == 1)
        *rankScore += 2;
      else if (choices[1] == 2 && choices[2] == 1)
        *rankScore += 1;
      else if (choices[1] == 1 && choices[2] == 2)
        *rankScore += 1;
    }
  }
  
  // rank 1 calls these
  else if (nodeRank == 1){
    if (choices[nodeRank] == 0){
      if (choices[0] == 2 && choices[2] == 2)
        *rankScore += 2;
      else if (choices[0] == 0 && choices[2] == 2)
        *rankScore += 1;
      else if (choices[0] == 2 && choices[2] == 0)
        *rankScore += 1;
    }else if (choices[nodeRank] == 1){
      if (choices[0] == 0 && choices[2] == 0)
        *rankScore += 2;
      else if (choices[0] == 1 && choices[2] == 0)
        *rankScore += 1;
      else if (choices[0] == 0 && choices[2] == 1)
        *rankScore += 1;
    }else{
      if (choices[0] == 1 && choices[2] == 1)
        *rankScore += 2;
      else if (choices[0] == 2 && choices[2] == 1)
        *rankScore += 1;
      else if (choices[0] == 1 && choices[2] == 2)
        *rankScore += 1;
    }
  }

  // rank 2 calls these
  else if (nodeRank == 2){
    if (choices[nodeRank] == 0){
      if (choices[0] == 2 && choices[1] == 2)
        *rankScore += 2;
      else if (choices[0] == 0 && choices[1] == 2)
        *rankScore += 1;
      else if (choices[0] == 2 && choices[1] == 0)
        *rankScore += 1;
    }else if (choices[nodeRank] == 1){
      if (choices[0] == 0 && choices[1] == 0)
        *rankScore += 2;
      else if (choices[0] == 1 && choices[1] == 0)
        *rankScore += 1;
      else if (choices[0] == 0 && choices[1] == 1)
        *rankScore += 1;
    }else{
      if (choices[0] == 1 && choices[1] == 1)
        *rankScore += 2;
      else if (choices[0] == 2 && choices[1] == 1)
        *rankScore += 1;
      else if (choices[0] == 1 && choices[1] == 2)
        *rankScore += 1;
    }
  }
}