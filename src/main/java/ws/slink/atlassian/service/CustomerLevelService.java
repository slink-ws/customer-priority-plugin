package ws.slink.atlassian.service;

public class CustomerLevelService {

    private static final int MAX_LEVEL=4;

    public static int getLevel(String value) {
        int result = 0;
        for (int i = 1; i <= MAX_LEVEL; i++) {
            String list = ConfigService.instance().getList(i);
            if (null != list && !list.isEmpty() && list.contains(value)) {
                result = i;
                break;
            }
        }
        System.out.println("LEVEL FOR '" + value + "' is " + result);
        return result;
    }

    public boolean isCustom(String value) {
        return getLevel(value) > 0;
//        return levels.values().stream().flatMap(x -> x.stream()).collect(Collectors.toList()).contains(value);
    }

}
