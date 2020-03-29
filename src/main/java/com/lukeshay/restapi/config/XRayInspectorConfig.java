package com.lukeshay.restapi.config;

import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class XRayInspectorConfig extends AbstractXRayInterceptor {
	@Override
	protected Map<String, Map<String, Object>> generateMetadata(
			ProceedingJoinPoint proceedingJoinPoint, Subsegment subsegment
	) {
		return super.generateMetadata(proceedingJoinPoint, subsegment);
	}

	@Override
	@Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && bean(*Controller) && bean(*ServiceImpl) && bean(*filter)")
	public void xrayEnabledClasses() {
	}
}
