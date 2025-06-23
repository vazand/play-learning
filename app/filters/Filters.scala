package filters

import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter
//import play.api.mvc.Filter
import play.filters.cors.CORSFilter

import javax.inject.Inject


class Filters @Inject()(logging: LoggingFilter, CORSFilter: CORSFilter) extends HttpFilters {

  override def filters: Seq[EssentialFilter] = Seq(logging,CORSFilter)
}
