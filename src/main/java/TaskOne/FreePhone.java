package TaskOne;

public class FreePhone {
  String number;
  int country;
  String updatedAt;
  String dataHumans;
  String fullNumber;
  String countryText;
  String maxDate;
  String status;

  public FreePhone(String number, int country, String updatedAt, String dataHumans, String fullNumber, String countryText, String maxDate, String status) {
    this.number = number;
    this.country = country;
    this.updatedAt = updatedAt;
    this.dataHumans = dataHumans;
    this.fullNumber = fullNumber;
    this.countryText = countryText;
    this.maxDate = maxDate;
    this.status = status;
  }

  public String getNumber() {
    return number;
  }

  @Override
  public String toString() {
    return "FreePhone{" +
        "country=" + country +
        ", updated_at='" + updatedAt + '\'' +
        ", data_humans='" + dataHumans + '\'' +
        ", full_number='" + fullNumber + '\'' +
        ", country_text='" + countryText + '\'' +
        ", maxdate='" + maxDate + '\'' +
        ", status='" + status + '\'' +
        '}';
  }
}
