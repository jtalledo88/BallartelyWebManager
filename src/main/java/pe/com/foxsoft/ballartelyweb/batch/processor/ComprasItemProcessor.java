package pe.com.foxsoft.ballartelyweb.batch.processor;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import pe.com.foxsoft.ballartelyweb.jpa.data.Provider;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;

public class ComprasItemProcessor implements ItemProcessor<GuideHead, Provider>{

	private static final Logger logger = LoggerFactory.getLogger(ComprasItemProcessor.class);
	@Override
	public Provider process(GuideHead input) throws Exception {
		logger.debug("Objeto compra: " + ReflectionToStringBuilder.reflectionToString(input));
		return null;
	}

}
