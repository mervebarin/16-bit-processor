#include <stdio.h>
void main()
{
int a[4]={-5, 7, -2, -40}; /* Initialize vector a */
int b[4]={65, -23, 17, 1024}; /* Initialize vector b */
int i = 0, sum = 0;
for(i = 0; i < 4; i++)
{
sum = sum + a[i] * b[i];
}
printf("%d\n",sum);
}
