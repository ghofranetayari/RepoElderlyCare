package elderlycare.dto;

public class TrackingDTO {
    private Double latitudeInitial;
    private Double longitudeInitial;

    // Constructors
    public TrackingDTO() {
    }

    public TrackingDTO(Double latitudeInitial, Double longitudeInitial) {
        this.latitudeInitial = latitudeInitial;
        this.longitudeInitial = longitudeInitial;
    }

    // Getters and setters
    public Double getLatitudeInitial() {
        return latitudeInitial;
    }

    public void setLatitudeInitial(Double latitudeInitial) {
        this.latitudeInitial = latitudeInitial;
    }

    public Double getLongitudeInitial() {
        return longitudeInitial;
    }

    public void setLongitudeInitial(Double longitudeInitial) {
        this.longitudeInitial = longitudeInitial;
    }
}
