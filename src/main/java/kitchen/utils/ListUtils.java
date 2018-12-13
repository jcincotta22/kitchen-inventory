package kitchen.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static<T> List<T>[] partition(List<T> list, int n)
    {
        // get size of the list
        int size = list.size();

        // calculate number of partitions m of size n each
        int m = size / n;
        if (size % n != 0)
            m++;

        // create m empty lists and initialize it using List.subList()
        List<T>[] partition = new ArrayList[m];
        for (int i = 0; i < m; i++)
        {
            int fromIndex = i*n;
            int toIndex = (i*n + n < size) ? (i*n + n) : size;

            partition[i] = new ArrayList(list.subList(fromIndex, toIndex));
        }

        // return the lists
        return partition;
    }
}
