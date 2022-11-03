package sawfowl.minerefill.api;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface MineAPI {

	public Set<Mine> getMines();

	public Map<String, Mine> getEditableMines();

	public void removeEditableMine(String editor);

	public void addEditableMine(String editor, Mine mine);

	public Optional<Mine> getEditableMine(String editor);

	public void saveMine(Mine mine);

	public void deleteMine(Mine mine);

}
