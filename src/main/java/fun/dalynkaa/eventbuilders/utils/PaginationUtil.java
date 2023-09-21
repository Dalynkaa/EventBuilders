package fun.dalynkaa.eventbuilders.utils;

import java.util.*;

public class PaginationUtil {
    public static <T> List<T> paginateList(List<T> list, int page, int pageSize) {
        int totalItems = list.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        if (startIndex >= totalItems) {
            return Collections.emptyList(); // Return an empty list if the startIndex exceeds the total number of items
        }

        return list.subList(startIndex, endIndex);
    }
    public static int getTotalPages(int totalItems, int pageSize) {
        return (int) Math.ceil((double) totalItems / pageSize);
    }
}
