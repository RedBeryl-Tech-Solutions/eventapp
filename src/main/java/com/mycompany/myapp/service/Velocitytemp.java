package com.mycompany.myapp.service;
import java.io.StringWriter;

import com.mycompany.myapp.domain.SubscriptionDetails;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mycompany.myapp.repository.SubscriptionDetailsRepository;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Velocitytemp {

        private final SubscriptionDetailsRepository subscriptionDetailsRepository;

        @Autowired
        public Velocitytemp(SubscriptionDetailsRepository subscriptionDetailsRepository) {
            this.subscriptionDetailsRepository = subscriptionDetailsRepository;
        }
        public String generateTemplate(SubscriptionDetails subscriptionDetails) throws Exception {
            //List<SubscriptionDetails> subscriptionDetailsList = subscriptionDetailsRepository.findAll();

            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init();
            Template template = velocityEngine.getTemplate("src/main/resources/prog.vm");


            VelocityContext context = new VelocityContext();
            //for (SubscriptionDetails subscriptionDetails : subscriptionDetailsList) {
            context.put("name", subscriptionDetails.getSubscriptionName());
            context.put("expiryDate", subscriptionDetails.getSubscriptionExpiryDate().toString());
            context.put("remarks", subscriptionDetails.getAdditionalComments());

            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            System.out.println(writer.toString());

            return writer.toString();
            //}
            //return null;
        }
    }





