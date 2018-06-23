package wvw.owl2rl.custom.config;

public class OntologyConfig {

	private String content;
	private String syntax;

	public OntologyConfig() {
	}

	public OntologyConfig(String path, String contents, String syntax) {
		this.content = contents;

		this.syntax = syntax;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSyntax() {
		return syntax;
	}

	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public boolean isComplete() {
		return content != null && syntax != null;
	}
	
	public String getRequiredFields() {
		return "'content', 'syntax'";
	}
}
