package unikom.skripsi.angga.masyarakat.model;

import java.util.List;

public class RekomendasiTempatModel {
    private String jarakPengungsian;
    private String tempat;
    private String lokasi;
    private String lat;
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getJarakPengungsian() {
        return jarakPengungsian;
    }

    public void setJarakPengungsian(String jarakPengungsian) {
        this.jarakPengungsian = jarakPengungsian;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public class RekomendasiTempatDataModel{
        private String message;
        private List<RekomendasiTempatModel> results;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<RekomendasiTempatModel> getResults() {
            return results;
        }

        public void setResults(List<RekomendasiTempatModel> results) {
            this.results = results;
        }
    }
}
