#include <stdio.h>

// Structure to store process details
struct Process {
    int pid;
    int burstTime;
    int priority;
    int waitingTime;
    int turnAroundTime;
};

// Function to sort processes based on priority
void sortByPriority(struct Process p[], int n) {
    struct Process temp;
    for(int i = 0; i < n - 1; i++) {
        for(int j = i + 1; j < n; j++) {
            if(p[i].priority > p[j].priority) { // lower value = higher priority
                temp = p[i];
                p[i] = p[j];
                p[j] = temp;
            }
        }
    }
}

// Function to calculate waiting time
void calculateWaitingTime(struct Process p[], int n) {
    p[0].waitingTime = 0;
    for(int i = 1; i < n; i++) {
        p[i].waitingTime = p[i - 1].waitingTime + p[i - 1].burstTime;
    }
}

// Function to calculate turnaround time
void calculateTurnAroundTime(struct Process p[], int n) {
    for(int i = 0; i < n; i++) {
        p[i].turnAroundTime = p[i].waitingTime + p[i].burstTime;
    }
}

int main() {
    int n;
    printf("Enter number of processes: ");
    scanf("%d", &n);

    struct Process p[n];

    // Input
    for(int i = 0; i < n; i++) {
        p[i].pid = i + 1;
        printf("\nProcess %d\n", i + 1);
        printf("Enter Burst Time: ");
        scanf("%d", &p[i].burstTime);
        printf("Enter Priority: ");
        scanf("%d", &p[i].priority);
    }

    // Sort processes by priority
    sortByPriority(p, n);

    // Calculate times
    calculateWaitingTime(p, n);
    calculateTurnAroundTime(p, n);

    // Output
    float totalWT = 0, totalTAT = 0;

    printf("\nPID\tBT\tPriority\tWT\tTAT\n");
    for(int i = 0; i < n; i++) {
        printf("%d\t%d\t%d\t\t%d\t%d\n",
               p[i].pid,
               p[i].burstTime,
               p[i].priority,
               p[i].waitingTime,
               p[i].turnAroundTime);

        totalWT += p[i].waitingTime;
        totalTAT += p[i].turnAroundTime;
    }

    printf("\nAverage Waiting Time = %.2f", totalWT / n);
    printf("\nAverage Turnaround Time = %.2f\n", totalTAT / n);

    return 0;
}