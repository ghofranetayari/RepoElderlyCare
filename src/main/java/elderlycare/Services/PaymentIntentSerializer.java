package elderlycare.Services;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.StripeResponse;

import java.io.IOException;

public class PaymentIntentSerializer extends StdSerializer<PaymentIntent> {

    public PaymentIntentSerializer() {
        this(null);
    }

    public PaymentIntentSerializer(Class<PaymentIntent> t) {
        super(t);
    }

    @Override
    public void serialize(PaymentIntent value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        // Serialize other properties as needed

        // Serialize StripeResponse
        StripeResponse lastResponse = value.getLastResponse();
        if (lastResponse != null) {
            gen.writeObjectFieldStart("lastResponse");
            // Serialize relevant properties of StripeResponse
            gen.writeStringField("message", lastResponse.getClass().getEnclosingMethod().toGenericString());
            ;
            // Add other fields as needed
            gen.writeEndObject();
        }

        gen.writeEndObject();
    }
}
