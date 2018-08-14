package unikom.skripsi.angga.masyarakat.model;

import java.util.List;

public class SungaiModel{
    private String sungai;
    private String tma;

    public String getSungai() {
        return sungai;
    }

    public void setSungai(String sungai) {
        this.sungai = sungai;
    }

    public String getTma() {
        return tma;
    }

    public void setTma(String tma) {
        this.tma = tma;
    }

    public class SungaiDataModel{
        private List<SungaiModel> results;
        private String message;

        public List<SungaiModel> getResults() {
            return results;
        }

        public void setResults(List<SungaiModel> results) {
            this.results = results;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
