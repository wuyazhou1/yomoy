package com.nsc.AmoskiUser.client;

/*

@Configuration
public class DruidConfig {
	*/
/**
	 * ServletRegistrationBean,
	 * @see com.alibaba.druid.support.http.ResourceServlet
	 * @return
	 *//*

	@Bean
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean druid = new ServletRegistrationBean();
		druid.setServlet(new StatViewServlet());
		druid.setUrlMappings(Arrays.asList("/druid/*"));
		
		Map<String,String> params = new HashMap<>();
		params.put("loginUsername", "admin");
		params.put("loginPassword", "admin");
		druid.setInitParameters(params);
		return druid;
	}
	
	*/
/**
	 * @see com.alibaba.druid.support.http.WebStatFilter
	 * @return
	 *//*

	@Bean
	public FilterRegistrationBean webStatFilter(){
		FilterRegistrationBean fitler = new FilterRegistrationBean();
		fitler.setFilter(new WebStatFilter());
		fitler.setUrlPatterns(Arrays.asList("/*"));
		fitler.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return fitler;
	}
}
*/
