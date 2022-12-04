import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.StringTokenizer;

public class SingleAndMultiThread {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader  br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int step = Integer.parseInt(st.nextToken());
        int seed = Integer.parseInt(st.nextToken());
        int N    = Integer.parseInt(st.nextToken());
        int M    = Integer.parseInt(st.nextToken());
        int root = seed;

        // M개의 리스트, N개의 random 정수 값
        numList[] nLists = new numList[M];
        for (int i=0; i<M;i++) {
            nLists[i] = new numList(N, seed++);
        }

        System.out.printf("Step-1:seed(%d),N(%d),M(%d)\n", root, N, M);

        if (step >= 2)
            System.out.printf("Step-2:Gap(%d)\n", Gap(nLists, M, N));

        if (step >= 3) {
            MyThread_1 thread = new MyThread_1(nLists, M, N);
            thread.start();
            thread.join();
        }

        if (step >= 4) {
            MyThread_2[] thread2 = new MyThread_2[M];
            int result = 0;
            for (int i=0; i<M; i++) {
                thread2[i] = new MyThread_2(nLists[i], M, N);
                thread2[i].start();
                thread2[i].join();
                result += thread2[i].result;
            }
            System.out.printf("Step-4:Multi Threading Gap(%d)", result);
        }
    }

    public static int Gap(numList[] nLists, int M, int N) {
        int result = 0;
        for(int i=0; i<M; i++) {
            numList nList = nLists[i];
            for (int j=0; j<N-1; j++) {
                result += Math.abs(nList.arrayList.get(j + 1)
                                - nList.arrayList.get(j)
                );
            }
        }
        return result;
    }

    public static class numList {
        ArrayList<Integer> arrayList;
        Random random;
        numList (int N, int seed) {
            random = new Random();
            random.setSeed(seed);
            arrayList = new ArrayList<>();
            for (int i=0; i<N; i++) {
                arrayList.add(random.nextInt(N));
            }
        }
    }

    public static class MyThread_1 extends Thread {
        numList[] nLists;
        int M, N;
        int result = 0;


        MyThread_1 (numList[] nLists, int M, int N) {
            this.nLists = nLists;
            this.N = N;
            this.M = M;
        }

        public void run() {
            for(int i=0; i<M;i++){
                numList nList = nLists[i];
                nList.arrayList.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return Integer.compare(o1, o2);
                    }
                });
                for(int j=0; j<N-1;j++){
                    result += Math.abs(
                            nList.arrayList.get(j + 1)
                                    - nList.arrayList.get(j)
                    );
                }
            }
            System.out.printf("Step-3:Single Threading Gap(%d)\n", result);
        }


    }

    public static class MyThread_2 extends Thread {
        numList nList;
        int M, N;
        int result = 0;


        MyThread_2 (numList nList, int M, int N) {
            this.nList = nList;
            this.N = N;
            this.M = M;
        }

        public void run() {
            nList.arrayList.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return Integer.compare(o1, o2);
                }
            });
            for (int i=0; i<N-1; i++) {
                result += Math.abs(
                        nList.arrayList.get(i+1)
                        - nList.arrayList.get(i)
                );
            }
        }
    }
}