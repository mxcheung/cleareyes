package com.maxcheung.contribution.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;

import com.maxcheung.contribution.ContributionSchedule;
import com.maxcheung.contribution.translate.ContributionTranslator;
import com.maxcheung.employer.domain.Contribution;
import com.maxcheung.payment.builder.ContributionPaymentsBuilder;
import com.maxcheung.payment.builder.PaymentMasterBuilder;
import com.maxcheung.payment.domain.ContributionPayment;
import com.maxcheung.payment.domain.PaymentMaster;

public class PaymentMasterBuilderTest {

	// with type declaration
	// MathOperation addition = (BigDecimal a, BigDecimal b) -> a + b;

	private static final String PAY_TO_SUBACCOUNT = "Pay to subaccount";
	private ContributionSchedule contributionSchedule;
	private List<Contribution> contributions;
    private Boolean taxable; 


	@Test
	public void test() {
		ContributionSchedule contributionSchedule = new ContributionSchedule();
		assertNotNull(contributionSchedule);
	}

	@Before
	public void setUp() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("contribution/CS_EMP100008015_13031418073465.xml").getFile());
		System.out.println(file.getAbsolutePath());

		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(ContributionSchedule.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			contributionSchedule = (ContributionSchedule) jaxbUnmarshaller.unmarshal(file);
			System.out.println(contributionSchedule);

			assertNotNull(contributionSchedule);

			assertEquals(15, contributionSchedule.getDetailRecord().size());
			contributions = ContributionTranslator.from(contributionSchedule);

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testPaymentMasterBuilder() {

		PaymentMasterBuilder paymentMasterBuilder = new PaymentMasterBuilder();
		
		
		ContributionPaymentsBuilder contributionPaymentsBuilder = new ContributionPaymentsBuilder();
		
		// @formatter:off
		
		taxable = true;
		
		paymentMasterBuilder = paymentMasterBuilder
				.setOtherInstructions(PAY_TO_SUBACCOUNT)
				.setTaxable(taxable)
				.copyContributionPaymentsFrom( contributionPaymentsBuilder.copyFrom(contributions)
				.build());

		
		// @formatter:on
		PaymentMaster paymentMaster = paymentMasterBuilder.build();

		assertNotNull(paymentMaster);
		assertEquals(15, paymentMaster.getContributionPayments().size());
		assertEquals(PAY_TO_SUBACCOUNT, paymentMaster.getOtherInstructions());
		assertEquals(taxable, paymentMaster.getTaxable());

		
		


		
	}

}
