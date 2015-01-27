package my.lib;

public interface UtilConstants {
    /* MyLog에서 사용하는 상수 */
	public static final String LOG_TAG_1 = "XXXXX";
	public static final String LOG_TAG_2 = "yeojoy";


	/* TimeAgo에서 사용한 상수 */
	public static final long ONE_SECOND     = 1000l;
    public static final long ONE_MINUTE     = ONE_SECOND * 60l;
    public static final long ONE_HOUR       = ONE_MINUTE * 60l;
    public static final long ONE_DAY        = ONE_HOUR * 24l;
    public static final long ONE_MONTH      = ONE_DAY * 30l;
    public static final long ONE_YEAR       = ONE_DAY * 365l;
    
    /** 5초 */
    public static final long MIN_SECONDS    = ONE_SECOND * 5l;
}
