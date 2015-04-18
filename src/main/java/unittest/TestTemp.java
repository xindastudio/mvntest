package unittest;
import java.sql.Timestamp;

public class TestTemp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println(genStatSql());
		System.out.println(new Timestamp(System.currentTimeMillis()));
	}

	private static String genStatSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("select region_code, count(*), sum(case when is_compliance = '1' then 1 else 0 end) from (");
		sb.append(genQualifyOrNotSql(true));
		sb.append(" union all ");
		sb.append(genQualifyOrNotSql(false));
		sb.append(") group by region_code");
		return sb.toString();
	}

	private static String genQualifyOrNotSql(boolean qualify) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from t_http_speed_test t where (1=1)");
		if (!qualify) {
			sb.append(" and not exists (select 1 from t_http_speed_test t2");
			sb.append(" where t2.is_compliance = '1'");
			sb.append(" and t2.user_id = t.user_id");
			sb.append(genValidFilterSql("t2"));
			sb.append(genOtherFilterSql("t2")).append(")");
		}
		sb.append(" and not exists (select 1 from t_http_speed_test t2 where t2.id > t.id");
		if (qualify) {
			sb.append(" and t2.is_compliance = '1'");
		}
		sb.append(" and t2.user_id = t.user_id ");
		sb.append(genValidFilterSql("t2"));
		sb.append(genOtherFilterSql("t2")).append(")");
		if (qualify) {
			sb.append(" and  t.is_compliance = '1'");
		}
		sb.append(genValidFilterSql("t"));
		sb.append(genOtherFilterSql("t"));
		return sb.toString();
	}

	private static String genOtherFilterSql(String t) {
		StringBuilder sb = new StringBuilder();
		sb.append(" and (").append(t)
				.append(".end_Time >= to_date('2013-04-01','yyyy-MM-dd'))");
		sb.append(" and (").append(t)
				.append(".end_Time < to_date('2013-05-01','yyyy-MM-dd'))");
		return sb.toString();
	}

	private static String genValidFilterSql(String t) {
		StringBuilder sb = new StringBuilder();
		sb.append(" and exists (select 1 from T_REGION r where r.status = '1'");
		sb.append(" and ").append(t).append(".region_code = r.regionCode)");
		sb.append(" and exists (select 1 from T_SUBREGION s where s.status = '1'");
		sb.append(" and ").append(t)
				.append(".sub_region_code = s.subregionCode)");
		sb.append(" and ").append(t).append(".region_Code is not null");
		sb.append(" and ").append(t).append(".sub_Region_Code is not null");
		sb.append(" and ").append(t).append(".user_Id is not null");
		sb.append(" and ").append(t).append(".user_Account is not null");
		sb.append(" and ").append(t).append(".apply_Speed_Dn is not null");
		sb.append(" and ").append(t).append(".avg_Speed is not null");
		sb.append(" and ").append(t).append(".avg_Speed > 0");
		sb.append(" and ").append(t).append(".failure is not null");
		sb.append(" and ").append(t).append(".failure = 0");
		return sb.toString();
	}

}
