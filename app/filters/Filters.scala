package filters

import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter
import play.filters.cors.CORSFilter

import javax.inject.Inject


class Filters @Inject()(logging: LoggingFilter, corsFilter: CORSFilter) extends HttpFilters {

  override def filters: Seq[EssentialFilter] = Seq(logging,corsFilter)
}
